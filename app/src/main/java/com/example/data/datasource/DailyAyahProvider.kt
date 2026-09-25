package com.example.data.datasource

import com.example.data.model.DailyAyah

object DailyAyahProvider {

    val dailyAyahs = listOf(
        DailyAyah(
            surahNumber = 2,
            ayahNumber = 186,
            surahNameBangla = "আল-বাক্বারাহ",
            arabicText = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ",
            banglaPronunciation = "ওয়া ইযা- সাআলাকা ‘ইবা-দী ‘আন্নী ফাইন্নী ক্বারীব, উজী-বু দা‘ওয়াতাদ দা-‘ই ইযা- দা‘আ-নি",
            banglaTranslation = "আর আমার বান্দারা যখন আপনার কাছে আমার ব্যাপারে জিজ্ঞেস করে, নিশ্চয় আমি অতি নিকটে। আহ্বানকারী যখনই আমাকে ডাকে, আমি তার ডাকে সাড়া দেই।"
        ),
        DailyAyah(
            surahNumber = 94,
            ayahNumber = 6,
            surahNameBangla = "আল-ইনশিরাহ",
            arabicText = "إِنَّ مَعَ الْعُسْرِ يُسْرًا",
            banglaPronunciation = "ইন্না মা‘আল ‘উসরি ইউসরা-",
            banglaTranslation = "নিশ্চয় কষ্টের সাথেই স্বস্তি রয়েছে।"
        ),
        DailyAyah(
            surahNumber = 2,
            ayahNumber = 255,
            surahNameBangla = "আল-বাক্বারাহ (আয়াতুল কুরসী)",
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ",
            banglaPronunciation = "আল্লা-হু লা- ইলা-হা ইল্লা- হুওয়াল হাইয়্যুল ক্বাইয়্যূম, লা- তা’খুযুহু সিনাতুওঁ ওয়ালা- নাওম",
            banglaTranslation = "আল্লাহ! তিনি ছাড়া কোনো উপাস্য নেই, তিনি চিরঞ্জীব, চিরস্থায়ী নিয়ন্ত্রক। তাঁকে তন্দ্রাও স্পর্শ করতে পারে না, নিদ্রাও নয়।"
        ),
        DailyAyah(
            surahNumber = 3,
            ayahNumber = 139,
            surahNameBangla = "আলে-ইমরান",
            arabicText = "وَلَا تَهِنُوا وَلَا تَحْزَنُوا وَأَنتُمُ الْأَعْلَوْنَ إِن كُنتُم مُّؤْمِنِينَ",
            banglaPronunciation = "ওয়ালা- তাহিনূ ওয়ালা- তাহযানূ ওয়া আনতুমুল আ‘লাওনা ইন কুনতুম মু’মিনীন",
            banglaTranslation = "তোমরা হতাশ হয়ো না এবং দুঃখিতও হয়ো না, তোমরাই বিজয়ী হবে যদি তোমরা মুমিন হও।"
        ),
        DailyAyah(
            surahNumber = 13,
            ayahNumber = 28,
            surahNameBangla = "আর-রা'দ",
            arabicText = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            banglaPronunciation = "আলা- বিযিকরিল্লা-হি তাতমা’ইন্নুল ক্বুলূব",
            banglaTranslation = "জেনে রাখো, আল্লাহর স্মরণের মাধ্যমেই অন্তরসমূহ প্রশান্ত হয়।"
        ),
        DailyAyah(
            surahNumber = 65,
            ayahNumber = 3,
            surahNameBangla = "আত-ত্বালাক",
            arabicText = "وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ",
            banglaPronunciation = "ওয়া মাইঁ ইয়াতাওয়াক্কাল ‘আলাল্লা-হি ফাহুওয়া হাসবুহু",
            banglaTranslation = "আর যে ব্যক্তি আল্লাহর উপর তাওয়াক্কুল (ভরসা) করে, তার জন্য তিনিই যথেষ্ট।"
        )
    )

    fun getTodayAyah(): DailyAyah {
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val index = dayOfYear % dailyAyahs.size
        return dailyAyahs[index]
    }
}
