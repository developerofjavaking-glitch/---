package com.example.data.datasource

import com.example.data.model.Ayah

object PreloadedQuranData {

    private val preloadedAyahs: Map<Int, List<Ayah>> = mapOf(
        // Surah Al-Fatihah (1)
        1 to listOf(
            Ayah(
                surahNumber = 1,
                ayahNumber = 1,
                arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                banglaPronunciation = "বিসমিল্লাহির রাহমানির রাহিম",
                banglaTranslation = "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001001.mp3"
            ),
            Ayah(
                surahNumber = 1,
                ayahNumber = 2,
                arabicText = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                banglaPronunciation = "আলহামদু লিল্লাহি রাব্বিল ‘আলামীন",
                banglaTranslation = "যাবতীয় প্রশংসা একমাত্র আল্লাহ তা‘আলার, যিনি সমস্ত সৃষ্টিজগতের পালনকর্তা।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001002.mp3"
            ),
            Ayah(
                surahNumber = 1,
                ayahNumber = 3,
                arabicText = "الرَّحْمَٰنِ الرَّحِيمِ",
                banglaPronunciation = "আর-রাহমানির রাহীম",
                banglaTranslation = "যিনি পরম করুণাময় ও অতি দয়ালু।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001003.mp3"
            ),
            Ayah(
                surahNumber = 1,
                ayahNumber = 4,
                arabicText = "مَالِكِ يَوْمِ الدِّينِ",
                banglaPronunciation = "মালিকি ইয়াওমিদ্দীন",
                banglaTranslation = "যিনি বিচার দিবসের মালিক।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001004.mp3"
            ),
            Ayah(
                surahNumber = 1,
                ayahNumber = 5,
                arabicText = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                banglaPronunciation = "ইয়্যাকা না‘বুদু ওয়া ইয়্যাকা নাস্তা‘ঈন",
                banglaTranslation = "আমরা একমাত্র তোমারই ইবাদত করি এবং শুধুমাত্র তোমারই সাহায্য প্রার্থনা করি।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001005.mp3"
            ),
            Ayah(
                surahNumber = 1,
                ayahNumber = 6,
                arabicText = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                banglaPronunciation = "ইহদিনাস সিরাতাল মুস্তাক্বীম",
                banglaTranslation = "আমাদের সরল-সঠিক পথ প্রদর্শন কর।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001006.mp3"
            ),
            Ayah(
                surahNumber = 1,
                ayahNumber = 7,
                arabicText = "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                banglaPronunciation = "সিরাতাল্লাযীনা আন‘আমতা ‘আলাইহিম, গাইরিল মাগদূবি ‘আলাইহিম ওয়ালাদ্দোয়াল্লীন",
                banglaTranslation = "তাদের পথ, যাদেরকে তুমি অনুগ্রহ দান করেছ; তাদের পথ নয় যাদের ওপর তোমার ক্রোধ বর্ষিত হয়েছে এবং যারা পথভ্রষ্ট হয়েছে।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001007.mp3"
            )
        ),

        // Surah Al-Asr (103)
        103 to listOf(
            Ayah(
                surahNumber = 103,
                ayahNumber = 1,
                arabicText = "وَالْعَصْرِ",
                banglaPronunciation = "ওয়াল ‘আসর",
                banglaTranslation = "কালের শপথ,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/103001.mp3"
            ),
            Ayah(
                surahNumber = 103,
                ayahNumber = 2,
                arabicText = "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ",
                banglaPronunciation = "ইন্নাল ইনসানা লাফী খুসর",
                banglaTranslation = "নিশ্চয় সমস্ত মানুষ ক্ষতির মধ্যে নিমজ্জিত,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/103002.mp3"
            ),
            Ayah(
                surahNumber = 103,
                ayahNumber = 3,
                arabicText = "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ",
                banglaPronunciation = "ইল্লাল্লাযীনা আমানূ ওয়া ‘আমিলুস সালিহাতি ওয়া তাওয়াসাও বিল হাক্বক্বি ওয়া তাওয়াসাও বিস সাবর",
                banglaTranslation = "তবে তারা ছাড়া যারা ঈমান এনেছে, সৎকাজ করেছে এবং পরস্পরকে সত্যের উপদেশ দিয়েছে ও ধৈর্যের উপদেশ দিয়েছে।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/103003.mp3"
            )
        ),

        // Surah Al-Fil (105)
        105 to listOf(
            Ayah(
                surahNumber = 105,
                ayahNumber = 1,
                arabicText = "أَلَمْ تَرَ كَيْفَ فَعَلَ رَبُّكَ بِأَصْحَابِ الْفِيلِ",
                banglaPronunciation = "আলাম তারা কাইফা ফা‘আলা রাব্বুকা বি আসহাবিল ফীল",
                banglaTranslation = "আপনি কি দেখেননি আপনার পালনকর্তা হস্তীধারীদের সাথে কি ব্যবহার করেছিলেন?",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/105001.mp3"
            ),
            Ayah(
                surahNumber = 105,
                ayahNumber = 2,
                arabicText = "أَلَمْ يَجْعَلْ كَيْدَهُمْ فِي تَضْلِيلٍ",
                banglaPronunciation = "আলাম ইয়াজ‘আল কায়দাহুম ফী তাদ্বলীল",
                banglaTranslation = "তিনি কি তাদের চক্রান্ত ব্যর্থ করে দেননি?",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/105002.mp3"
            ),
            Ayah(
                surahNumber = 105,
                ayahNumber = 3,
                arabicText = "وَأَرْسَلَ عَلَيْهِمْ طَيْرًا أَبَابِيلَ",
                banglaPronunciation = "ওয়া আরসালা ‘আলাইহিম তাইরান আবাবীল",
                banglaTranslation = "তিনি তাদের ওপর ঝাঁকে ঝাঁকে আবাবিল পাখি প্রেরণ করেছিলেন,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/105003.mp3"
            ),
            Ayah(
                surahNumber = 105,
                ayahNumber = 4,
                arabicText = "تَرْمِيهِم بِحِجَارَةٍ مِّن سِجِّيلٍ",
                banglaPronunciation = "তারমীহিম বিহিজারাতিম মিন সিজ্জীল",
                banglaTranslation = "যারা তাদের ওপর পোড়ামাটির পাথর নিক্ষেপ করছিল,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/105004.mp3"
            ),
            Ayah(
                surahNumber = 105,
                ayahNumber = 5,
                arabicText = "فَجَعَلَهُمْ كَعَصْفٍ مَّأْكُولٍ",
                banglaPronunciation = "ফাজা‘আলাহুম কা‘আসফিম মা’কূল",
                banglaTranslation = "অতঃপর তিনি তাদেরকে ভক্ষিত তৃণসদৃশ করে দিয়েছিলেন।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/105005.mp3"
            )
        ),

        // Surah Quraysh (106)
        106 to listOf(
            Ayah(
                surahNumber = 106,
                ayahNumber = 1,
                arabicText = "لِإِيلَافِ قُرَيْشٍ",
                banglaPronunciation = "লি ঈলাফি কুরাইশ",
                banglaTranslation = "কুরাইশদের আসক্তির কারণে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/106001.mp3"
            ),
            Ayah(
                surahNumber = 106,
                ayahNumber = 2,
                arabicText = "إِيلَافِهِمْ رِحْلَةَ الشِّتَاءِ وَالصَّيْفِ",
                banglaPronunciation = "ঈলাফিহিম রিহলাতাশ শিতা-ই ওয়াস সইফ",
                banglaTranslation = "শীত ও গ্রীষ্মকালের তাদের ভ্রমণের অভ্যাসের কারণে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/106002.mp3"
            ),
            Ayah(
                surahNumber = 106,
                ayahNumber = 3,
                arabicText = "فَلْيَعْبُدُوا رَبَّ هَٰذَا الْبَيْتِ",
                banglaPronunciation = "ফালয়া‘বুদূ রাব্বা হাযাল বাইত",
                banglaTranslation = "অতএব তারা যেন এই গৃহের (কাবা) রবের ইবাদত করে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/106003.mp3"
            ),
            Ayah(
                surahNumber = 106,
                ayahNumber = 4,
                arabicText = "الَّذِي أَطْعَمَهُم مِّن جُوعٍ وَآمَنَهُم مِّنْ خَوْفٍ",
                banglaPronunciation = "আল্লাযী আত‘আমাহুম মিন জু‘ইওঁ ওয়া আমানাহুম মিন খাওফ",
                banglaTranslation = "যিনি তাদেরকে ক্ষুধায় আহার দিয়েছেন এবং ভয়-ভীতি থেকে নিরাপত্তা দান করেছেন।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/106004.mp3"
            )
        ),

        // Surah Al-Kawthar (108)
        108 to listOf(
            Ayah(
                surahNumber = 108,
                ayahNumber = 1,
                arabicText = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ",
                banglaPronunciation = "ইন্না আ‘তাইনা-কাল কাওসার",
                banglaTranslation = "নিশ্চয় আমি আপনাকে কাওসার (অফুরন্ত কল্যাণ) দান করেছি।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/108001.mp3"
            ),
            Ayah(
                surahNumber = 108,
                ayahNumber = 2,
                arabicText = "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
                banglaPronunciation = "ফাসাল্লি লিরাব্বিকা ওয়ানহার",
                banglaTranslation = "অতএব আপনার রবের উদ্দেশ্যে নামাজ আদায় করুন এবং কোরবানি করুন।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/108002.mp3"
            ),
            Ayah(
                surahNumber = 108,
                ayahNumber = 3,
                arabicText = "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
                banglaPronunciation = "ইন্না শানি-আকা হুওয়াল আবতার",
                banglaTranslation = "নিশ্চয় আপনার শত্রুই তো নির্বংশ, লেজকাটা।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/108003.mp3"
            )
        ),

        // Surah Al-Kafirun (109)
        109 to listOf(
            Ayah(
                surahNumber = 109,
                ayahNumber = 1,
                arabicText = "قُلْ يَا أَيُّهَا الْكَافِرُونَ",
                banglaPronunciation = "ক্বুল ইয়া আইয়ুহাল কাফিরূন",
                banglaTranslation = "বলুন, হে কাফির সম্প্রদায়,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/109001.mp3"
            ),
            Ayah(
                surahNumber = 109,
                ayahNumber = 2,
                arabicText = "لَا أَعْبُدُ مَا تَعْبُدُونَ",
                banglaPronunciation = "লা-আ‘বুদু মা তা‘বুদূন",
                banglaTranslation = "আমি তাদের ইবাদত করি না যাদের তোমরা পূজা কর,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/109002.mp3"
            ),
            Ayah(
                surahNumber = 109,
                ayahNumber = 3,
                arabicText = "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ",
                banglaPronunciation = "ওয়ালা-আনতুম ‘আবিদূনা মা-আ‘বুদ",
                banglaTranslation = "এবং তোমরাও তাঁর ইবাদতকারী নও যাঁর ইবাদত আমি করি,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/109003.mp3"
            ),
            Ayah(
                surahNumber = 109,
                ayahNumber = 4,
                arabicText = "وَلَا أَنَا عَابِدٌ مَّا عَبَدتُّمْ",
                banglaPronunciation = "ওয়ালা-আনা ‘আবিদুম মা ‘আবাদতুম",
                banglaTranslation = "এবং আমি তাদের উপাসক হব না যাদের তোমরা পূজা করেছ,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/109004.mp3"
            ),
            Ayah(
                surahNumber = 109,
                ayahNumber = 5,
                arabicText = "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ",
                banglaPronunciation = "ওয়ালা-আনতুম ‘আবিদূনা মা-আ‘বুদ",
                banglaTranslation = "আর তোমরাও তাঁর ইবাদতকারী নও যাঁর ইবাদত আমি করি,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/109005.mp3"
            ),
            Ayah(
                surahNumber = 109,
                ayahNumber = 6,
                arabicText = "لَكُمْ دِينُكُمْ وَلِيَ دِينِ",
                banglaPronunciation = "লাকুম দীনুকুম ওয়ালিয়া দীন",
                banglaTranslation = "তোমাদের দ্বীন তোমাদের জন্য, আর আমার দ্বীন আমার জন্য।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/109006.mp3"
            )
        ),

        // Surah An-Nasr (110)
        110 to listOf(
            Ayah(
                surahNumber = 110,
                ayahNumber = 1,
                arabicText = "إِذَا جَاءَ نَصْرُ اللَّهِ وَالْفَتْحُ",
                banglaPronunciation = "ইযা জা-আ নাসরুল্লাহি ওয়াল ফাতহ",
                banglaTranslation = "যখন আল্লাহর সাহায্য ও বিজয় আসবে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/110001.mp3"
            ),
            Ayah(
                surahNumber = 110,
                ayahNumber = 2,
                arabicText = "وَرَأَيْتَ النَّاسَ يَدْخُلُونَ فِي دِينِ اللَّهِ أَفْوَاجًا",
                banglaPronunciation = "ওয়া রাআইতান নাসা ইয়াদখুলূনা ফী দীনিল্লাহি আফওয়াজা",
                banglaTranslation = "এবং আপনি মানুষকে দলে দলে আল্লাহর দ্বীনে প্রবেশ করতে দেখবেন,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/110002.mp3"
            ),
            Ayah(
                surahNumber = 110,
                ayahNumber = 3,
                arabicText = "فَسَبِّحْ بِحَمْدِ رَبِّكَ وَاسْتَغْفِرْهُ ۚ إِنَّهُ كَانَ تَوَّابًا",
                banglaPronunciation = "ফাসাব্বিহ বিহামদি রাব্বিকা ওয়াসতাগফিরহু, ইন্নাহু কানা তাওওয়াবা",
                banglaTranslation = "তখন আপনি আপনার রবের প্রশংসাসহ পবিত্রতা ঘোষণা করুন এবং তাঁর কাছে ক্ষমা প্রার্থনা করুন; নিশ্চয় তিনি তওবা কবুলকারী।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/110003.mp3"
            )
        ),

        // Surah Al-Ikhlas (112)
        112 to listOf(
            Ayah(
                surahNumber = 112,
                ayahNumber = 1,
                arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ",
                banglaPronunciation = "ক্বুল হুওয়াল্লাহু আহাদ",
                banglaTranslation = "বলুন, তিনিই আল্লাহ, যিনি একক ও অদ্বিতীয়,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/112001.mp3"
            ),
            Ayah(
                surahNumber = 112,
                ayahNumber = 2,
                arabicText = "اللَّهُ الصَّمَدُ",
                banglaPronunciation = "আল্লাহুস সামাদ",
                banglaTranslation = "আল্লাহ কারও মুখাপেক্ষী নন, সকলেই তাঁর মুখাপেক্ষী,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/112002.mp3"
            ),
            Ayah(
                surahNumber = 112,
                ayahNumber = 3,
                arabicText = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
                banglaPronunciation = "লাম ইয়ালিদ ওয়া লাম ইয়ূলাদ",
                banglaTranslation = "তিনি কাউকে জন্ম দেননি এবং তিনিও কারো থেকে জন্ম নেননি,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/112003.mp3"
            ),
            Ayah(
                surahNumber = 112,
                ayahNumber = 4,
                arabicText = "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                banglaPronunciation = "ওয়া লাম ইয়াকুল্লাহু কুফুওয়ান আহাদ",
                banglaTranslation = "এবং তাঁর সমতুল্য কেউই নেই।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/112004.mp3"
            )
        ),

        // Surah Al-Falaq (113)
        113 to listOf(
            Ayah(
                surahNumber = 113,
                ayahNumber = 1,
                arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
                banglaPronunciation = "ক্বুল আ‘ঊযু বিরাব্বিল ফালাক্ব",
                banglaTranslation = "বলুন, আমি আশ্রয় প্রার্থনা করছি উষার রবের নিকট,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/113001.mp3"
            ),
            Ayah(
                surahNumber = 113,
                ayahNumber = 2,
                arabicText = "مِن شَرِّ مَا خَلَقَ",
                banglaPronunciation = "মিন শাররি মা খালাক্ব",
                banglaTranslation = "তিনি যা সৃষ্টি করেছেন তার অনিষ্ট হতে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/113002.mp3"
            ),
            Ayah(
                surahNumber = 113,
                ayahNumber = 3,
                arabicText = "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
                banglaPronunciation = "ওয়া মিন শাররি গাসিক্বিন ইযা ওয়াক্বাব",
                banglaTranslation = "এবং অন্ধকারের অনিষ্ট হতে যখন তা গভীর হয়,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/113003.mp3"
            ),
            Ayah(
                surahNumber = 113,
                ayahNumber = 4,
                arabicText = "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
                banglaPronunciation = "ওয়া মিন শাররিন নাফ্ফা-সা-তি ফিল ‘উক্বাদ",
                banglaTranslation = "এবং গিরায় ফুঁকদানকারী জাদুকারিণীদের অনিষ্ট হতে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/113004.mp3"
            ),
            Ayah(
                surahNumber = 113,
                ayahNumber = 5,
                arabicText = "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
                banglaPronunciation = "ওয়া মিন শাররি হাসিদিন ইযা হাসাদ",
                banglaTranslation = "এবং হিংসুকের অনিষ্ট হতে যখন সে হিংসা করে।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/113005.mp3"
            )
        ),

        // Surah An-Nas (114)
        114 to listOf(
            Ayah(
                surahNumber = 114,
                ayahNumber = 1,
                arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
                banglaPronunciation = "ক্বুল আ‘ঊযু বিরাব্বিন নাস",
                banglaTranslation = "বলুন, আমি আশ্রয় প্রার্থনা করছি মানুষের প্রতিপালকের কাছে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/114001.mp3"
            ),
            Ayah(
                surahNumber = 114,
                ayahNumber = 2,
                arabicText = "مَلِكِ النَّاسِ",
                banglaPronunciation = "মালিকিন নাস",
                banglaTranslation = "মানুষের অধিপতির কাছে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/114002.mp3"
            ),
            Ayah(
                surahNumber = 114,
                ayahNumber = 3,
                arabicText = "إِلَٰهِ النَّاسِ",
                banglaPronunciation = "ইলাহিন নাস",
                banglaTranslation = "মানুষের প্রকৃত উপাস্যের কাছে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/114003.mp3"
            ),
            Ayah(
                surahNumber = 114,
                ayahNumber = 4,
                arabicText = "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
                banglaPronunciation = "মিন শাররিল ওয়াসওয়াসিল খান্নাস",
                banglaTranslation = "আত্মগোপনকারী কুমন্ত্রণাদাতার অনিষ্ট থেকে,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/114004.mp3"
            ),
            Ayah(
                surahNumber = 114,
                ayahNumber = 5,
                arabicText = "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
                banglaPronunciation = "আল্লাযী ইউওয়াসউইসু ফী সুদূরিন নাস",
                banglaTranslation = "যে মানুষের অন্তরে কুমন্ত্রণা দেয়,",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/114005.mp3"
            ),
            Ayah(
                surahNumber = 114,
                ayahNumber = 6,
                arabicText = "مِنَ الْجِنَّةِ وَالنَّاسِ",
                banglaPronunciation = "মিনাল জিন্নাতি ওয়ান নাস",
                banglaTranslation = "জ্বিনের মধ্য থেকে এবং মানুষের মধ্য থেকে।",
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/114006.mp3"
            )
        )
    )

    fun getPreloadedSurah(surahNumber: Int): List<Ayah>? = preloadedAyahs[surahNumber]

    fun hasPreloaded(surahNumber: Int): Boolean = preloadedAyahs.containsKey(surahNumber)
}
