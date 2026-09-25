package com.example.data.util

object BanglaTransliterationUtil {

    /**
     * Converts Quranic Arabic text into readable Bengali pronunciation (বাংলা উচ্চারণ).
     * Handles common Arabic consonants, vowel marks (harakat), shaddah (দ্বিত্ব),
     * tanween, and sukuns.
     */
    fun arabicToBangla(arabic: String): String {
        if (arabic.isBlank()) return ""

        // Common high-frequency phrases fast-path
        val clean = arabic.trim()
        if (clean.contains("بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ") || clean.contains("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")) {
            return "বিসমিল্লাহির রাহমানির রাহিম"
        }

        val sb = StringBuilder()
        var i = 0
        val len = clean.length

        while (i < len) {
            val c = clean[i]

            // Check two-char combos or special markers
            when {
                // Alif with wasla at start
                c == '\u0671' -> {
                    // ٱ
                    sb.append("আ")
                    i++
                }
                // Standard letters
                c == 'ا' || c == 'أ' || c == 'إ' || c == 'آ' -> {
                    sb.append(if (c == 'إ') "ই" else "আ")
                    i++
                }
                c == 'ب' -> { sb.append("ব"); i++ }
                c == 'ت' || c == 'ة' -> { sb.append("ত"); i++ }
                c == 'ث' -> { sb.append("ছ"); i++ }
                c == 'ج' -> { sb.append("জ"); i++ }
                c == 'ح' -> { sb.append("হ"); i++ }
                c == 'خ' -> { sb.append("খ"); i++ }
                c == 'د' -> { sb.append("দ"); i++ }
                c == 'ذ' -> { sb.append("যাল"); i++ } // often rendered as 'য'
                c == 'ر' -> { sb.append("র"); i++ }
                c == 'ز' -> { sb.append("য"); i++ }
                c == 'س' -> { sb.append("স"); i++ }
                c == 'ش' -> { sb.append("শ"); i++ }
                c == 'ص' -> { sb.append("সোয়াদ"); i++ } // or 'স'
                c == 'ض' -> { sb.append("দোয়াদ"); i++ } // or 'দ'
                c == 'ط' -> { sb.append("ত্ব"); i++ }
                c == 'ظ' -> { sb.append("জ্ব"); i++ }
                c == 'ع' -> { sb.append("‘আ"); i++ }
                c == 'غ' -> { sb.append("গাইর"); i++ } // or 'গ'
                c == 'ف' -> { sb.append("ফ"); i++ }
                c == 'ق' -> { sb.append("ক্ব"); i++ }
                c == 'ك' -> { sb.append("ক"); i++ }
                c == 'ل' -> { sb.append("ল"); i++ }
                c == 'م' -> { sb.append("ম"); i++ }
                c == 'ن' -> { sb.append("ন"); i++ }
                c == 'ه' || c == 'ہ' -> { sb.append("হ"); i++ }
                c == 'و' -> { sb.append("ওয়া"); i++ }
                c == 'ي' || c == 'ى' || c == 'ئ' -> { sb.append("ইয়া"); i++ }
                c == 'ء' -> { sb.append("’"); i++ }

                // Harakat
                c == '\u064E' -> { // Fatha
                    sb.append("া")
                    i++
                }
                c == '\u0650' -> { // Kasra
                    sb.append("ি")
                    i++
                }
                c == '\u064F' -> { // Damma
                    sb.append("ু")
                    i++
                }
                c == '\u0652' -> { // Sukun
                    sb.append("্")
                    i++
                }
                c == '\u0651' -> { // Shaddah
                    sb.append("্")
                    i++
                }
                c == '\u064B' -> { // Tanween Fatha
                    sb.append("ান")
                    i++
                }
                c == '\u064D' -> { // Tanween Kasra
                    sb.append("িন")
                    i++
                }
                c == '\u064C' -> { // Tanween Damma
                    sb.append("উন")
                    i++
                }
                c == '\u0670' -> { // Dagger alif
                    sb.append("া")
                    i++
                }
                c == ' ' -> {
                    sb.append(" ")
                    i++
                }
                else -> {
                    // Skip or keep punctuation
                    if (c.isWhitespace()) sb.append(" ")
                    i++
                }
            }
        }

        // Clean up repeated vowel marks or weird joins
        return cleanupBengali(sb.toString())
    }

    private fun cleanupBengali(text: String): String {
        return text
            .replace("াা", "া")
            .replace("িি", "ি")
            .replace("ুু", "ু")
            .replace("্্", "্")
            .replace("  ", " ")
            .trim()
    }
}
