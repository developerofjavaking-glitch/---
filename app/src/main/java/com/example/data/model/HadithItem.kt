package com.example.data.model

data class HadithItem(
    val id: Int,
    val category: String,
    val titleBangla: String,
    val arabicText: String,
    val banglaTranslation: String,
    val narratorBangla: String,
    val bookBangla: String,
    val hadithNumber: String,
    val gradeBangla: String = "সহীহ",
    val lessonsBangla: String = ""
)
