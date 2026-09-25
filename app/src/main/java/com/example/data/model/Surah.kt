package com.example.data.model

data class Surah(
    val number: Int,
    val nameBangla: String,
    val nameArabic: String,
    val nameEnglish: String,
    val meaningBangla: String,
    val totalAyat: Int,
    val revelationType: RevelationType,
    val revelationOrder: Int = 0
) {
    val isMakki: Boolean get() = revelationType == RevelationType.MAKKI
}

enum class RevelationType(val banglaLabel: String, val englishLabel: String) {
    MAKKI("মাক্কী", "Makki"),
    MADANI("মাদানী", "Madani")
}

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val banglaPronunciation: String,
    val banglaTranslation: String,
    val audioUrl: String = ""
)

data class Reciter(
    val id: String,
    val nameBangla: String,
    val nameArabic: String,
    val primaryBaseUrl: String,
    val fallbackBaseUrl: String = ""
)

data class LastReadInfo(
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameBangla: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class DailyAyah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameBangla: String,
    val arabicText: String,
    val banglaPronunciation: String,
    val banglaTranslation: String
)

data class PlaybackState(
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val isFullSurah: Boolean = false,
    val currentSurahNumber: Int = 0,
    val currentAyahNumber: Int = 0,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
    val errorMessage: String? = null,
    val speed: Float = 1.0f,
    val reciterName: String = "শায়খ মিশারী রশীদ আল-আফাসী"
)
