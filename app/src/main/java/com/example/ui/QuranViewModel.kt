package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.QuranAudioPlayer
import com.example.data.datasource.HadithProvider
import com.example.data.datasource.SurahVirtuesProvider
import com.example.data.model.Ayah
import com.example.data.model.DailyAyah
import com.example.data.model.HadithItem
import com.example.data.model.LastReadInfo
import com.example.data.model.PlaybackState
import com.example.data.model.Reciter
import com.example.data.model.RevelationType
import com.example.data.model.Surah
import com.example.data.model.SurahVirtue
import com.example.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SurahFilter {
    ALL, MAKKI, MADANI, VIRTUES, HADITH, BOOKMARKED
}

sealed interface VersesUiState {
    data object Loading : VersesUiState
    data class Success(val verses: List<Ayah>) : VersesUiState
    data class Error(val message: String) : VersesUiState
}

class QuranViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuranRepository(application)
    val audioPlayer = QuranAudioPlayer(application)

    val playbackState: StateFlow<PlaybackState> = audioPlayer.playbackState

    private val _allSurahs = MutableStateFlow<List<Surah>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(SurahFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _bookmarkedSurahs = MutableStateFlow<Set<Int>>(emptySet())
    val bookmarkedSurahs = _bookmarkedSurahs.asStateFlow()

    private val _selectedSurah = MutableStateFlow<Surah?>(null)
    val selectedSurah = _selectedSurah.asStateFlow()

    // Surah Virtues state
    private val _allVirtues = MutableStateFlow<List<SurahVirtue>>(emptyList())
    val allVirtues = _allVirtues.asStateFlow()

    private val _selectedVirtue = MutableStateFlow<SurahVirtue?>(null)
    val selectedVirtue = _selectedVirtue.asStateFlow()

    // Hadith state
    private val _selectedHadithCategory = MutableStateFlow("সকল হাদিস")
    val selectedHadithCategory = _selectedHadithCategory.asStateFlow()

    private val _hadithSearchQuery = MutableStateFlow("")
    val hadithSearchQuery = _hadithSearchQuery.asStateFlow()

    private val _favoriteHadithIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteHadithIds = _favoriteHadithIds.asStateFlow()

    private val _versesUiState = MutableStateFlow<VersesUiState>(VersesUiState.Loading)
    val versesUiState = _versesUiState.asStateFlow()

    // Reciter Selection
    private val _selectedReciter = MutableStateFlow(repository.getSelectedReciter())
    val selectedReciter = _selectedReciter.asStateFlow()

    // Last Read Persistence
    private val _lastRead = MutableStateFlow<LastReadInfo?>(repository.getLastRead())
    val lastRead = _lastRead.asStateFlow()

    // Daily Ayah
    val dailyAyah: DailyAyah = repository.getDailyAyah()

    // Reader customization
    private val _arabicFontSize = MutableStateFlow(25f)
    val arabicFontSize = _arabicFontSize.asStateFlow()

    private val _banglaFontSize = MutableStateFlow(16f)
    val banglaFontSize = _banglaFontSize.asStateFlow()

    private val _showPronunciation = MutableStateFlow(false)
    val showPronunciation = _showPronunciation.asStateFlow()

    private val _showTranslation = MutableStateFlow(true)
    val showTranslation = _showTranslation.asStateFlow()

    init {
        _allSurahs.value = repository.getAllSurahs()
        _bookmarkedSurahs.value = repository.getBookmarkedSurahNumbers()
        _allVirtues.value = SurahVirtuesProvider.allVirtues
        _favoriteHadithIds.value = repository.getFavoriteHadithIds()
    }

    val filteredSurahs: StateFlow<List<Surah>> = combine(
        _allSurahs,
        _searchQuery,
        _selectedFilter,
        _bookmarkedSurahs
    ) { surahs, query, filter, bookmarks ->
        surahs.filter { surah ->
            val matchesFilter = when (filter) {
                SurahFilter.ALL -> true
                SurahFilter.MAKKI -> surah.revelationType == RevelationType.MAKKI
                SurahFilter.MADANI -> surah.revelationType == RevelationType.MADANI
                SurahFilter.BOOKMARKED -> bookmarks.contains(surah.number)
                SurahFilter.VIRTUES -> true
                SurahFilter.HADITH -> true
            }

            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                val q = query.trim().lowercase()
                surah.nameBangla.lowercase().contains(q) ||
                surah.nameArabic.contains(q) ||
                surah.nameEnglish.lowercase().contains(q) ||
                surah.meaningBangla.lowercase().contains(q) ||
                surah.number.toString().contains(q)
            }

            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredVirtues: StateFlow<List<SurahVirtue>> = combine(
        _allVirtues,
        _searchQuery
    ) { virtues, query ->
        if (query.isBlank()) {
            virtues
        } else {
            val q = query.trim().lowercase()
            virtues.filter {
                it.surahNameBangla.lowercase().contains(q) ||
                it.surahNameArabic.contains(q) ||
                it.quickHighlight.lowercase().contains(q) ||
                it.surahNumber.toString().contains(q)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredHadiths: StateFlow<List<HadithItem>> = combine(
        _selectedHadithCategory,
        _hadithSearchQuery,
        _favoriteHadithIds
    ) { cat, query, favs ->
        HadithProvider.allHadiths.filter { hadith ->
            val matchesCat = when (cat) {
                "সকল হাদিস" -> true
                "পছন্দের হাদিস" -> favs.contains(hadith.id)
                else -> hadith.category == cat
            }

            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                val q = query.trim().lowercase()
                hadith.titleBangla.lowercase().contains(q) ||
                hadith.banglaTranslation.lowercase().contains(q) ||
                hadith.arabicText.contains(q) ||
                hadith.bookBangla.lowercase().contains(q) ||
                hadith.narratorBangla.lowercase().contains(q) ||
                hadith.hadithNumber.contains(q)
            }

            matchesCat && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectVirtue(virtue: SurahVirtue?) {
        _selectedVirtue.value = virtue
    }

    fun setHadithCategory(category: String) {
        _selectedHadithCategory.value = category
    }

    fun setHadithSearchQuery(query: String) {
        _hadithSearchQuery.value = query
    }

    fun toggleFavoriteHadith(id: Int) {
        repository.toggleFavoriteHadith(id)
        _favoriteHadithIds.value = repository.getFavoriteHadithIds()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: SurahFilter) {
        _selectedFilter.value = filter
    }

    fun toggleBookmark(surahNumber: Int) {
        repository.toggleSurahBookmark(surahNumber)
        _bookmarkedSurahs.value = repository.getBookmarkedSurahNumbers()
    }

    fun selectSurah(surah: Surah?) {
        _selectedSurah.value = surah
        if (surah != null) {
            recordLastRead(surah.number, 1, surah.nameBangla)
            loadSurahVerses(surah.number)
        }
    }

    fun recordLastRead(surahNumber: Int, ayahNumber: Int, surahNameBangla: String) {
        repository.saveLastRead(surahNumber, ayahNumber, surahNameBangla)
        _lastRead.value = repository.getLastRead()
    }

    fun selectReciter(reciter: Reciter) {
        repository.setSelectedReciterId(reciter.id)
        _selectedReciter.value = reciter

        // If full surah was currently playing, switch to new reciter immediately
        val currentState = playbackState.value
        if (currentState.isFullSurah && currentState.isPlaying) {
            playFullSurah(currentState.currentSurahNumber)
        }
    }

    fun loadSurahVerses(surahNumber: Int) {
        viewModelScope.launch {
            _versesUiState.value = VersesUiState.Loading
            val result = repository.getSurahVerses(surahNumber)
            result.onSuccess { list ->
                _versesUiState.value = VersesUiState.Success(list)
            }.onFailure { err ->
                _versesUiState.value = VersesUiState.Error(
                    err.message ?: "আয়াতসমূহ লোড করতে ব্যর্থ হয়েছে। ইন্টারনেট সংযোগ পরীক্ষা করে পুনরায় চেষ্টা করুন।"
                )
            }
        }
    }

    fun playFullSurah(surahNumber: Int) {
        val reciter = _selectedReciter.value
        val urls = repository.getFullSurahAudioUrls(surahNumber, reciter)
        audioPlayer.playFullSurah(surahNumber, urls, reciter.nameBangla)
    }

    fun playAyah(ayah: Ayah) {
        val sNum = String.format("%03d", ayah.surahNumber)
        val aNum = String.format("%03d", ayah.ayahNumber)
        val primaryUrl = if (ayah.audioUrl.isNotBlank()) ayah.audioUrl else "https://everyayah.com/data/Alafasy_128kbps/$sNum$aNum.mp3"
        val fallbackUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/${(ayah.surahNumber - 1) * 10 + ayah.ayahNumber}.mp3"
        audioPlayer.playAyah(ayah.surahNumber, ayah.ayahNumber, primaryUrl, fallbackUrl)
    }

    fun playDailyAyah(daily: DailyAyah) {
        val sNum = String.format("%03d", daily.surahNumber)
        val aNum = String.format("%03d", daily.ayahNumber)
        val url = "https://everyayah.com/data/Alafasy_128kbps/$sNum$aNum.mp3"
        audioPlayer.playAyah(daily.surahNumber, daily.ayahNumber, url)
    }

    fun pauseAudio() = audioPlayer.pause()
    fun resumeAudio() = audioPlayer.resume()
    fun seekAudio(positionMs: Int) = audioPlayer.seekTo(positionMs)
    fun stopAudio() = audioPlayer.stop()
    fun skipForward10Sec() = audioPlayer.skipForward10Sec()
    fun skipBackward10Sec() = audioPlayer.skipBackward10Sec()
    fun setPlaybackSpeed(speed: Float) = audioPlayer.setPlaybackSpeed(speed)
    fun retryAudio() = audioPlayer.retryPlayback()

    fun updateArabicFontSize(size: Float) {
        _arabicFontSize.value = size.coerceIn(18f, 38f)
    }

    fun updateBanglaFontSize(size: Float) {
        _banglaFontSize.value = size.coerceIn(13f, 24f)
    }

    fun toggleShowPronunciation() {
        _showPronunciation.value = !_showPronunciation.value
    }

    fun toggleShowTranslation() {
        _showTranslation.value = !_showTranslation.value
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
