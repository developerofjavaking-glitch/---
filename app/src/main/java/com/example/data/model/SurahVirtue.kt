package com.example.data.model

data class SurahVirtue(
    val surahNumber: Int,
    val surahNameBangla: String,
    val surahNameArabic: String,
    val meaningBangla: String,
    val revelationType: RevelationType,
    val totalAyat: Int,
    val quickHighlight: String,
    val historyContextBangla: String,
    val revelationReasonBangla: String,
    val arabicReferenceText: String,
    val virtuesBangla: String,
    val blessingsAndActionBangla: String,
    val keyThemesBangla: String
)
