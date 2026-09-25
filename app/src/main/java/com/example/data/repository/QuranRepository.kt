package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.datasource.DailyAyahProvider
import com.example.data.datasource.PreloadedQuranData
import com.example.data.datasource.RecitersProvider
import com.example.data.datasource.SurahListProvider
import com.example.data.model.Ayah
import com.example.data.model.DailyAyah
import com.example.data.model.LastReadInfo
import com.example.data.model.Reciter
import com.example.data.model.Surah
import com.example.data.util.BanglaTransliterationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class QuranRepository(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    // In-memory cache for fast repeated switching
    private val cachedSurahs = mutableMapOf<Int, List<Ayah>>()

    // Shared preferences for bookmarks, last read, settings
    private val prefs = context.getSharedPreferences("quran_prefs", Context.MODE_PRIVATE)

    fun getAllSurahs(): List<Surah> = SurahListProvider.surahs

    fun getSurah(number: Int): Surah? = SurahListProvider.getSurahByNumber(number)

    fun isSurahBookmarked(surahNumber: Int): Boolean {
        val bookmarks = prefs.getStringSet("bookmarked_surahs", emptySet()) ?: emptySet()
        return bookmarks.contains(surahNumber.toString())
    }

    fun toggleSurahBookmark(surahNumber: Int): Boolean {
        val bookmarks = (prefs.getStringSet("bookmarked_surahs", emptySet()) ?: emptySet()).toMutableSet()
        val newState = if (bookmarks.contains(surahNumber.toString())) {
            bookmarks.remove(surahNumber.toString())
            false
        } else {
            bookmarks.add(surahNumber.toString())
            true
        }
        prefs.edit().putStringSet("bookmarked_surahs", bookmarks).apply()
        return newState
    }

    fun getBookmarkedSurahNumbers(): Set<Int> {
        val bookmarks = prefs.getStringSet("bookmarked_surahs", emptySet()) ?: emptySet()
        return bookmarks.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun getFavoriteHadithIds(): Set<Int> {
        val favs = prefs.getStringSet("favorite_hadiths", emptySet()) ?: emptySet()
        return favs.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun toggleFavoriteHadith(id: Int): Boolean {
        val favs = (prefs.getStringSet("favorite_hadiths", emptySet()) ?: emptySet()).toMutableSet()
        val newState = if (favs.contains(id.toString())) {
            favs.remove(id.toString())
            false
        } else {
            favs.add(id.toString())
            true
        }
        prefs.edit().putStringSet("favorite_hadiths", favs).apply()
        return newState
    }

    // Last Read Persistence
    fun saveLastRead(surahNumber: Int, ayahNumber: Int, surahNameBangla: String) {
        prefs.edit()
            .putInt("last_read_surah", surahNumber)
            .putInt("last_read_ayah", ayahNumber)
            .putString("last_read_name", surahNameBangla)
            .putLong("last_read_time", System.currentTimeMillis())
            .apply()
    }

    fun getLastRead(): LastReadInfo? {
        val surahNum = prefs.getInt("last_read_surah", -1)
        if (surahNum == -1) return null
        val ayahNum = prefs.getInt("last_read_ayah", 1)
        val name = prefs.getString("last_read_name", "") ?: ""
        val time = prefs.getLong("last_read_time", 0L)
        return LastReadInfo(surahNum, ayahNum, name, time)
    }

    // Selected Reciter Persistence
    fun getSelectedReciter(): Reciter {
        val id = prefs.getString("selected_reciter_id", "alafasy") ?: "alafasy"
        return RecitersProvider.getReciterById(id)
    }

    fun setSelectedReciterId(id: String) {
        prefs.edit().putString("selected_reciter_id", id).apply()
    }

    fun getDailyAyah(): DailyAyah {
        return DailyAyahProvider.getTodayAyah()
    }

    suspend fun getSurahVerses(surahNumber: Int): Result<List<Ayah>> = withContext(Dispatchers.IO) {
        // 1. Check in-memory cache
        cachedSurahs[surahNumber]?.let {
            return@withContext Result.success(it)
        }

        // 2. Check preloaded offline dataset
        PreloadedQuranData.getPreloadedSurah(surahNumber)?.let {
            cachedSurahs[surahNumber] = it
            return@withContext Result.success(it)
        }

        // 3. Fetch from AlQuran Cloud API (both Arabic uthmani and Bengali Muhiuddin Khan translation)
        try {
            val url = "https://api.alquran.cloud/v1/surah/$surahNumber/editions/quran-uthmani,bn.bengali"
            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("সার্ভার থেকে সুরা লোড করা সম্ভব হয়নি (কোড: ${response.code})"))
            }

            val body = response.body?.string() ?: ""
            if (body.isBlank()) {
                return@withContext Result.failure(Exception("খালি ডেটা পাওয়া গেছে"))
            }

            val json = JSONObject(body)
            val dataArray = json.optJSONArray("data")
            if (dataArray == null || dataArray.length() < 2) {
                return@withContext Result.failure(Exception("অনাকাঙ্ক্ষিত ডেটা ফরম্যাট"))
            }

            val arabicEd = dataArray.getJSONObject(0)
            val bengaliEd = dataArray.getJSONObject(1)

            val arabicAyahs = arabicEd.getJSONArray("ayahs")
            val bengaliAyahs = bengaliEd.getJSONArray("ayahs")

            val ayahList = mutableListOf<Ayah>()
            val count = minOf(arabicAyahs.length(), bengaliAyahs.length())

            for (i in 0 until count) {
                val arObj = arabicAyahs.getJSONObject(i)
                val bnObj = bengaliAyahs.getJSONObject(i)

                val ayahNum = arObj.getInt("numberInSurah")
                var arText = arObj.getString("text")

                // Remove Bismillah from ayah 1 if it is prefixed in non-Fatihah surahs
                if (surahNumber != 1 && ayahNum == 1) {
                    val bismillahPrefix = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ "
                    val bismillahPrefix2 = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ "
                    if (arText.startsWith(bismillahPrefix)) {
                        arText = arText.removePrefix(bismillahPrefix).trim()
                    } else if (arText.startsWith(bismillahPrefix2)) {
                        arText = arText.removePrefix(bismillahPrefix2).trim()
                    }
                }

                val bnTrans = bnObj.getString("text")
                val bnPronunciation = BanglaTransliterationUtil.arabicToBangla(arText)

                val surahFormatted = String.format("%03d", surahNumber)
                val ayahFormatted = String.format("%03d", ayahNum)
                val audioUrl = "https://everyayah.com/data/Alafasy_128kbps/$surahFormatted$ayahFormatted.mp3"

                ayahList.add(
                    Ayah(
                        surahNumber = surahNumber,
                        ayahNumber = ayahNum,
                        arabicText = arText,
                        banglaPronunciation = bnPronunciation,
                        banglaTranslation = bnTrans,
                        audioUrl = audioUrl
                    )
                )
            }

            if (ayahList.isNotEmpty()) {
                cachedSurahs[surahNumber] = ayahList
                return@withContext Result.success(ayahList)
            } else {
                return@withContext Result.failure(Exception("কোন আয়াত পাওয়া যায়নি"))
            }

        } catch (e: Exception) {
            Log.e("QuranRepository", "Error fetching surah $surahNumber", e)
            return@withContext Result.failure(e)
        }
    }

    /**
     * Returns a prioritized list of audio URLs for the full surah recitation.
     * Primary uses Mp3Quran CDN (ultra reliable Cloudflare, 3-digit surah),
     * with fallbacks to QuranicAudio and Islamic Network CDN.
     */
    fun getFullSurahAudioUrls(surahNumber: Int, reciter: Reciter): List<String> {
        val sNum = String.format("%03d", surahNumber)
        val list = mutableListOf<String>()

        list.add("${reciter.primaryBaseUrl}$sNum.mp3")
        if (reciter.fallbackBaseUrl.isNotBlank()) {
            list.add("${reciter.fallbackBaseUrl}$sNum.mp3")
        }

        // Additional fallback for Alafasy
        if (reciter.id == "alafasy") {
            list.add("https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee/$sNum.mp3")
            list.add("https://cdn.islamic.network/quran/audio-surah/128/ar.alafasy/$surahNumber.mp3")
        }

        return list
    }
}
