package revolhope.splanes.com.smartcam.helper;


import com.google.common.collect.ImmutableMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants
{

    // Contacts
    public static final String CONTACT_NAME = "NAME";
    public static final String CONTACT_PHONE = "PHONE";
    public static final String CONTACT_MAIL = "MAIL";
    public static final String CONTACT_LOCATION = "LOCATION";
    public static final String CONTACT_WEB = "WEB";

    // Camera
    public static final boolean AUTO_HIDE = true;
    public static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    public static final int UI_ANIMATION_DELAY = 300;

    // Intent request code to handle updating play services if needed.
    public static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    public static final int RC_HANDLE_CAMERA_PERM = 2;

    public static final boolean AUTO_FOCUS = true;
    public static final boolean USE_FLASH = false;
    public static final String TEXTREAD = "TextRead";

    // Database constants
    public static final String DB_NAME = "AppDatabase";
    public static final String TABLE_CONTACT = "table_contact";
    public static final String TABLE_TAG = "table_tag";
    public static final String TABLE_TAG_SECTION = "table_tag_section";
    public static final String TABLE_ICON = "table_icon";

    // Translation constants
    public static final String TRANSLATION_MODEL_NMT = "nmt";
    public static final String TRANSLATION_MODEL_BASE = "base";

    public static final String TEXT_TO_TRANSLATE = "toTranslate";
    public static final int MODE_TRANSLATE_TEXT = 0;
    public static final int MODE_TRANSLATE_OPTIONS = 1;
    public static final int MODE_TRANSLATE_OPTIONS_MODE = 2;
    public static final int MODE_TRANSLATE_SUPPORTED_LANG = 3;
    public static final int MODE_TRANSLATE_DETECT = 4;

    // REQ TRANSLATE PERMISSIONS
    public static final int REQ_HANDLE_INTERNET_PERM = 623;

    // Code-Language Map
    public static final Map<String, String> mapLanguages
            = ImmutableMap.<String, String>builder()
            .put("af","Afrikaans")
            .put("sq","Albanian")
            .put("am","Amharic")
            .put("ar","Arabic")
            .put("hy","Armenian")
            .put("az","Azeerbaijani")
            .put("eu","Basque")
            .put("be","Belarusian")
            .put("bn","Bengali")
            .put("bs","Bosnian")
            .put("bg","Bulgarian")
            .put("ca","Catalan")
            .put("ceb","Cebuano")
            .put("zh","Chinese")
            .put("zh-CN","Chinese (Simplified)")
            .put("zh-TW","Chinese (Traditional)")
            .put("co","Corsican")
            .put("hr","Croatian")
            .put("cs","Czech")
            .put("da","Danish")
            .put("nl","Dutch")
            .put("en","English")
            .put("eo","Esperanto")
            .put("et","Estonian")
            .put("fi","Finnish")
            .put("fr","French")
            .put("fy","Frisian")
            .put("gl","Galician")
            .put("ka","Georgian")
            .put("de","German")
            .put("el","Greek")
            .put("gu","Gujarati")
            .put("ht","Haitian Creole")
            .put("ha","Hausa")
            .put("haw","Hawaiian")
            .put("iw","Hebrew")
            .put("hi","Hindi")
            .put("hmn","Hmong")
            .put("hu","Hungarian")
            .put("is","Icelandic")
            .put("ig","Igbo")
            .put("id","Indonesian")
            .put("ga","Irish")
            .put("it","Italian")
            .put("ja","Japanese")
            .put("jw","Javanese")
            .put("kn","Kannada")
            .put("kk","Kazakh")
            .put("km","Khmer")
            .put("ko","Korean")
            .put("ku","Kurdish")
            .put("ky","Kyrgyz")
            .put("lo","Lao")
            .put("la","Latin")
            .put("lv","Latvian")
            .put("lt","Lithuanian")
            .put("lb","Luxembourgish")
            .put("mk","Macedonian")
            .put("mg","Malagasy")
            .put("ms","Malay")
            .put("ml","Malayalam")
            .put("mt","Maltese")
            .put("mi","Maori")
            .put("mr","Marathi")
            .put("mn","Mongolian")
            .put("my","Myanmar (Burmese)")
            .put("ne","Nepali")
            .put("no","Norwegian")
            .put("ny","Nyanja (Chichewa)")
            .put("ps","Pashto")
            .put("fa","Persian")
            .put("pl","Polish")
            .put("pt","Portuguese (Portugal, Brazil)")
            .put("pa","Punjabi")
            .put("ro","Romanian")
            .put("ru","Russian")
            .put("sm","Samoan")
            .put("gd","Scots Gaelic")
            .put("sr","Serbian")
            .put("st","Sesotho")
            .put("sn","Shona")
            .put("sd","Sindhi")
            .put("si","Sinhala (Sinhalese)")
            .put("sk","Slovak")
            .put("sl","Slovenian")
            .put("so","Somali")
            .put("es","Spanish")
            .put("su","Sundanese")
            .put("sw","Swahili")
            .put("sv","Swedish")
            .put("tl","Tagalog (Filipino)")
            .put("tg","Tajik")
            .put("ta","Tamil")
            .put("te","Telugu")
            .put("th","Thai")
            .put("tr","Turkish")
            .put("uk","Ukrainian")
            .put("ur","Urdu")
            .put("uz","Uzbek")
            .put("vi","Vietnamese")
            .put("cy","Welsh")
            .put("xh","Xhosa")
            .put("yi","Yiddish")
            .put("yo","Yoruba")
            .put("zu","Zulu")
            .build();



    private Constants() {}

    public static String[] getFormattedLang(@NotNull String[] languages)
    {
        int size = languages.length;
        String[] list = new String[size];
        String code;

        for (int i=0 ; i < size ; i++)
        {
            code = languages[i];
            list[i] = mapLanguages.get(code) + " (" + code + ")";
        }
        Arrays.sort(list);
        return list;
    }

    @Nullable
    public static String getLanguageCode(String value)
    {
        if(mapLanguages.containsValue(value))
        {
            for (String key : mapLanguages.keySet())
            {
                if(mapLanguages.get(key).equals(value))
                {
                    return key;
                }
            }
        }

        return null;
    }
}
