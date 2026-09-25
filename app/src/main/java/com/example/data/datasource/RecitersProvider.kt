package com.example.data.datasource

import com.example.data.model.Reciter

object RecitersProvider {

    val reciters = listOf(
        Reciter(
            id = "alafasy",
            nameBangla = "শায়খ মিশারী রশীদ আল-আফাসী",
            nameArabic = "مشاري راشد العفاسي",
            primaryBaseUrl = "https://server8.mp3quran.net/afs/",
            fallbackBaseUrl = "https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee/"
        ),
        Reciter(
            id = "sudais",
            nameBangla = "শায়খ আব্দুর রহমান আস-সুদাইস",
            nameArabic = "عبد الرحمن السديس",
            primaryBaseUrl = "https://server11.mp3quran.net/sds/",
            fallbackBaseUrl = "https://download.quranicaudio.com/quran/abdurrahmaan_as-sudays/"
        ),
        Reciter(
            id = "maher",
            nameBangla = "শায়খ মাহের আল-মুয়াইক্বলী",
            nameArabic = "ماهر المعيقلي",
            primaryBaseUrl = "https://server12.mp3quran.net/maher/",
            fallbackBaseUrl = "https://download.quranicaudio.com/quran/maher_almu3aiqly/year1440/"
        ),
        Reciter(
            id = "ghamidi",
            nameBangla = "শায়খ সা'দ আল-গামিদী",
            nameArabic = "سعد الغامدي",
            primaryBaseUrl = "https://server7.mp3quran.net/s_gmd/",
            fallbackBaseUrl = "https://download.quranicaudio.com/quran/sa3d_al-ghaamidee/complete/"
        )
    )

    fun getReciterById(id: String): Reciter {
        return reciters.find { it.id == id } ?: reciters.first()
    }
}
