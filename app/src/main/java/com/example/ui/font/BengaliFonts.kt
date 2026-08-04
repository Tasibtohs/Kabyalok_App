package com.example.ui.font

import android.content.Context
import android.graphics.Typeface
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.R

data class BengaliFontOption(
    val key: String,
    val name: String,
    val category: String, // "ক্যালিগ্রাফি", "আর্টিস্টিক", "ডেকোরেটিভ", "ধ্রুপদী", "আধুনিক"
    val description: String,
    val fontFamily: FontFamily,
    val assetFileName: String,
    val resId: Int
)

object BengaliFonts {

    private val rawFonts = listOf(
        // ১. ক্যালিগ্রাফি ও কাব্যিক
        BengaliFontOption(
            key = "galada",
            name = "গলদা ক্যালিগ্রাফি",
            category = "ক্যালিগ্রাফি",
            description = "বাঁকানো স্ট্রোকের শৈল্পিক রাজকীয় বাংলা ক্যালিগ্রাফি",
            fontFamily = FontFamily.Serif,
            assetFileName = "galada.ttf",
            resId = R.font.galada
        ),
        BengaliFontOption(
            key = "amita",
            name = "অমিতা অলংকারিক",
            category = "ক্যালিগ্রাফি",
            description = "কাব্যিক ও নান্দনিক সোয়াশ ক্যালিগ্রাফিক ডিজাইন",
            fontFamily = FontFamily.Serif,
            assetFileName = "amita.ttf",
            resId = R.font.amita
        ),
        BengaliFontOption(
            key = "great_vibes",
            name = "গ্রেট ভাইবস স্ক্রিপ্ট",
            category = "ক্যালিগ্রাফি",
            description = "অভিজাত ও সাবলীল রাজকীয় ক্যালিগ্রাফি",
            fontFamily = FontFamily.Serif,
            assetFileName = "great_vibes.ttf",
            resId = R.font.great_vibes
        ),
        BengaliFontOption(
            key = "sacramento",
            name = "স্যাক্রামেন্টো এলিগ্যান্ট",
            category = "ক্যালিগ্রাফি",
            description = "সূক্ষ্ম ও মিষ্টি রেখার নান্দনিক ক্যালিগ্রাফিক রূপ",
            fontFamily = FontFamily.Serif,
            assetFileName = "sacramento.ttf",
            resId = R.font.sacramento
        ),

        // ২. আর্ট ও হাতে লেখা (Handwritten)
        BengaliFontOption(
            key = "atma",
            name = "আত্মা আর্ট",
            category = "আর্টিস্টিক",
            description = "ব্যক্তিগত কবিতার ডায়েরির মতো হাতে লেখা প্রাণবন্ত স্টাইল",
            fontFamily = FontFamily.Serif,
            assetFileName = "atma.ttf",
            resId = R.font.atma
        ),
        BengaliFontOption(
            key = "caveat",
            name = "ক্যাভিয়াট হস্তলিপি",
            category = "আর্টিস্টিক",
            description = "সহজ ও সাবলীল ব্যক্তিগত হস্তলিপির সৌন্দর্য",
            fontFamily = FontFamily.Serif,
            assetFileName = "caveat.ttf",
            resId = R.font.caveat
        ),
        BengaliFontOption(
            key = "dancing_script",
            name = "ড্যান্সিং নোটস",
            category = "আর্টিস্টিক",
            description = "নৃত্যময় গতিশীল ছন্দময় হাতের লেখা",
            fontFamily = FontFamily.Serif,
            assetFileName = "dancing_script.ttf",
            resId = R.font.dancing_script
        ),
        BengaliFontOption(
            key = "pacifico",
            name = "প্যাসিফিকো রেট্রো",
            category = "আর্টিস্টিক",
            description = "বোল্ড ও রোমান্টিক আর্ট স্ক্রিপ্ট ফন্ট",
            fontFamily = FontFamily.Serif,
            assetFileName = "pacifico.ttf",
            resId = R.font.pacifico
        ),
        BengaliFontOption(
            key = "satisfy",
            name = "স্যাটিসফাই ডায়েরি",
            category = "আর্টিস্টিক",
            description = "কাব্যিক স্পর্শের ধ্রুপদী টেক্সচারযুক্ত স্ক্রিপ্ট",
            fontFamily = FontFamily.Serif,
            assetFileName = "satisfy.ttf",
            resId = R.font.satisfy
        ),

        // ৩. ডেকোরেটিভ ও ডিসপ্লে (Decorative)
        BengaliFontOption(
            key = "shrikhand",
            name = "শ্রীখণ্ড প্রিমিয়াম",
            category = "ডেকোরেটিভ",
            description = "উৎসবমুখর ও জমকালো হেডলাইনের অলংকৃত রূপ",
            fontFamily = FontFamily.Serif,
            assetFileName = "shrikhand.ttf",
            resId = R.font.shrikhand
        ),
        BengaliFontOption(
            key = "rozha_one",
            name = "রোঝা ড্রামাটিক",
            category = "ডেকোরেটিভ",
            description = "উচ্চ বৈপরীত্যপূর্ণ ড্রামাটিক বোল্ড ডিসপ্লে হরফ",
            fontFamily = FontFamily.Serif,
            assetFileName = "rozha_one.ttf",
            resId = R.font.rozha_one
        ),
        BengaliFontOption(
            key = "lobster",
            name = "লবস্টার অলংকারিক",
            category = "ডেকোরেটিভ",
            description = "বোল্ড রেট্রো ক্যালিগ্রাফিক স্ক্রিপ্ট ডিজাইন",
            fontFamily = FontFamily.Serif,
            assetFileName = "lobster.ttf",
            resId = R.font.lobster
        ),
        BengaliFontOption(
            key = "monoton",
            name = "মনোটন লাইনস",
            category = "ডেকোরেটিভ",
            description = "আধুনিক আর্ট ও লাইন আর্ট রেট্রো স্টাইল",
            fontFamily = FontFamily.Serif,
            assetFileName = "monoton.ttf",
            resId = R.font.monoton
        ),

        // ৪. ধ্রুপদী সাহিত্য (Classical & Literary)
        BengaliFontOption(
            key = "tiro_bangla",
            name = "তিরো বাংলা (রাজকীয়)",
            category = "ধ্রুপদী",
            description = "বই ও কবিতার কাব্যিক রাজকীয় ধ্রুপদী ফন্ট",
            fontFamily = FontFamily.Serif,
            assetFileName = "tiro_bangla.ttf",
            resId = R.font.tiro_bangla
        ),
        BengaliFontOption(
            key = "noto_serif_bengali",
            name = "কালপুরুষ সেরিফ",
            category = "ধ্রুপদী",
            description = "ঐতিহ্যবাহী ধ্রুপদী বাংলা সাহিত্যিক হরফ",
            fontFamily = FontFamily.Serif,
            assetFileName = "noto_serif_bengali.ttf",
            resId = R.font.noto_serif_bengali
        ),
        BengaliFontOption(
            key = "lohit_bengali",
            name = "লোহিত বাংলা",
            category = "ধ্রুপদী",
            description = "সহজপাঠ্য প্রথাগত বাংলা হরফ",
            fontFamily = FontFamily.Serif,
            assetFileName = "lohit_bengali.ttf",
            resId = R.font.lohit_bengali
        ),
        BengaliFontOption(
            key = "playfair_display",
            name = "প্লেফেয়ার ডিসপ্লে",
            category = "ধ্রুপদী",
            description = "রাজকীয় ধ্রুপদী ডিসপ্লে টাইপোগ্রাফি",
            fontFamily = FontFamily.Serif,
            assetFileName = "playfair_display.ttf",
            resId = R.font.playfair_display
        ),
        BengaliFontOption(
            key = "cinzel",
            name = "সিনজেল ক্লাসিক",
            category = "ধ্রুপদী",
            description = "ধ্রুপদী খোদাই করা অক্ষরের আভিজাত্য",
            fontFamily = FontFamily.Serif,
            assetFileName = "cinzel.ttf",
            resId = R.font.cinzel
        ),
        BengaliFontOption(
            key = "eb_garamond",
            name = "গ্যারামন্ড ক্লাসিক",
            category = "ধ্রুপদী",
            description = "ধ্রুপদী সাহিত্য ও বই প্রকাশের ফন্ট",
            fontFamily = FontFamily.Serif,
            assetFileName = "eb_garamond.ttf",
            resId = R.font.eb_garamond
        ),

        // ৫. আধুনিক ও ডিজিটাল (Modern & Digital)
        BengaliFontOption(
            key = "hind_siliguri",
            name = "হিন্দ শিলিগুড়ি",
            category = "আধুনিক",
            description = "পরিষ্কার ও সুপাঠ্য আধুনিক বাংলা টাইপোগ্রাফি",
            fontFamily = FontFamily.SansSerif,
            assetFileName = "hind_siliguri.ttf",
            resId = R.font.hind_siliguri
        ),
        BengaliFontOption(
            key = "anek_bangla",
            name = "অনেক বাংলা",
            category = "আধুনিক",
            description = "স্টাইলিশ ও সুবিন্যস্ত আধুনিক বাংলা হরফ",
            fontFamily = FontFamily.SansSerif,
            assetFileName = "anek_bangla.ttf",
            resId = R.font.anek_bangla
        ),
        BengaliFontOption(
            key = "mina",
            name = "মীনা সফট",
            category = "আধুনিক",
            description = "নরম ও সাবলীল গোলাকার প্রান্তের ফন্ট",
            fontFamily = FontFamily.SansSerif,
            assetFileName = "mina.ttf",
            resId = R.font.mina
        ),
        BengaliFontOption(
            key = "baloo_da_2",
            name = "বালু দা ২",
            category = "আধুনিক",
            description = "বোল্ড ও প্রফুল্ল গোলাকার হরফ",
            fontFamily = FontFamily.SansSerif,
            assetFileName = "baloo_da_2.ttf",
            resId = R.font.baloo_da_2
        ),
        BengaliFontOption(
            key = "noto_sans_bengali",
            name = "নোটো সান্স বাংলা",
            category = "আধুনিক",
            description = "মার্জিত ও ভারসাম্যপূর্ণ ডিজিটাল বাংলা ফন্ট",
            fontFamily = FontFamily.SansSerif,
            assetFileName = "noto_sans_bengali.ttf",
            resId = R.font.noto_sans_bengali
        )
    )

    private var initialized = false
    private var activeFonts: List<BengaliFontOption> = rawFonts

    val fonts: List<BengaliFontOption>
        get() = activeFonts

    fun init(context: Context) {
        if (initialized) return
        initialized = true
        val assets = context.applicationContext.assets

        activeFonts = rawFonts.map { fontOption ->
            val loadedFamily = try {
                val typeface = Typeface.createFromAsset(assets, "fonts/${fontOption.assetFileName}")
                FontFamily(typeface)
            } catch (e: Throwable) {
                if (fontOption.category == "আধুনিক") FontFamily.SansSerif else FontFamily.Serif
            }
            fontOption.copy(fontFamily = loadedFamily)
        }
    }

    fun getFontByKey(key: String): BengaliFontOption {
        return activeFonts.find { it.key == key } ?: activeFonts[0]
    }
}
