package revolhope.splanes.com.smartcam.helper.translate;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.LanguageListOption;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import revolhope.splanes.com.smartcam.helper.Constants;

public class TranslatorProcessor {

    public static String[] detectLanguage(String sourceText)
    {
        Translate translate = createTranslateService();
        List<Detection> detections = translate.detect(ImmutableList.of(sourceText));
        int size = detections.size();

        String[] result = new String[size];
        for (int i = 0 ; i < size ; i++)
        {
            result[i] = detections.get(i).getLanguage();
        }

        return result;
    }

    /**
     * Create Google Translate API Service
     * @return Google Translate Service
     */
    private static Translate createTranslateService()
    {
        return TranslateOptions.newBuilder().build().getService();
    }

    public static String translateText(String sourceText)
    {
        Translate translate = createTranslateService();
        Translation translation = translate.translate(sourceText);
        return translation.getTranslatedText();
    }

    /**
     * Translate the source text from source to target language.
     * Make sure that your project is whitelisted.
     *
     * @param sourceText source text to be translated
     * @param sourceLang source language of the text
     * @param targetLang target language of translated text
     */
    public static String translateTextWithOptionsAndModel(
            String sourceText,
            String sourceLang,
            String targetLang,
            boolean nmtModel)
    {
        Translate translate = createTranslateService();
        TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLang);
        TranslateOption tgtLang = TranslateOption.targetLanguage(targetLang);
        TranslateOption model = TranslateOption.model(nmtModel ?
                Constants.TRANSLATION_MODEL_NMT :
                Constants.TRANSLATION_MODEL_BASE);

        Translation translation = translate.translate(sourceText, srcLang, tgtLang, model);
        return translation.getTranslatedText();
    }

    /**
     * Translate the source text from source to target language.
     *
     * @param sourceText source text to be translated
     * @param sourceLang source language of the text
     * @param targetLang target language of translated text
     */
    public static String translateTextWithOptions(
            String sourceText,
            String sourceLang,
            String targetLang)
    {
        Translate translate = createTranslateService();
        TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLang);
        TranslateOption tgtLang = TranslateOption.targetLanguage(targetLang);

        Translation translation = translate.translate(sourceText, srcLang, tgtLang);
        return translation.getTranslatedText();
    }

    /**
     * Displays a list of supported languages and codes.
     *
     * @param tgtLang optional target language
     */
    public static List<String> getSupportedLanguages(String tgtLang)
    {
        Translate translate = createTranslateService();

        LanguageListOption target = LanguageListOption.targetLanguage(tgtLang);
        List<Language> languages = translate.listSupportedLanguages(target);

        List<String> langList = new ArrayList<>();
        for (Language language : languages)
        {
            langList.add(String.format("Name: %s, Code: %s", language.getName(), language.getCode()));
        }

        return langList;
    }
}
    