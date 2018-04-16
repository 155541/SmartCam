package revolhope.splanes.com.smartcam.helper.translate;

import android.os.AsyncTask;
import android.util.Log;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.ImmutableList;
import java.util.List;

import revolhope.splanes.com.smartcam.helper.Constants;

public class TranslateAsyncTask extends AsyncTask<String, Integer, String[]>
{

    private static final String TAG = "TranslateAsyncTask";
    private CallbackAsyncTask callbackAsyncTask;
    private int mode;

    public TranslateAsyncTask(CallbackAsyncTask callbackAsyncTask, int mode)
    {
        this.callbackAsyncTask = callbackAsyncTask;
        this.mode = mode;
    }

    @Override
    protected String[] doInBackground(String[] objects)
    {
        String[] result = null;

        int size;
        Translation translation;
        Translate translate = TranslateOptions.newBuilder().build().getService();
        Translate.TranslateOption srcLang;
        Translate.TranslateOption tgtLang;

        switch (mode)
        {
            case Constants.MODE_TRANSLATE_DETECT:

                List<Detection> detections = translate.detect(ImmutableList.of(objects[0]));
                size = detections.size();

                result = new String[size];
                for (int i = 0 ; i < size ; i++)
                {
                    result[i] = detections.get(i).getLanguage();
                }
                return result;

            case Constants.MODE_TRANSLATE_SUPPORTED_LANG:

                Translate.LanguageListOption target = Translate.LanguageListOption.targetLanguage(objects[0]);
                List<Language> languages = translate.listSupportedLanguages(target);

                size = languages.size();
                result = new String[size];
                for (int i = 0; i < size ; i++)
                {
                    result[i] = languages.get(i).getName();
                    Log.i(TAG, languages.get(i).getName() + " - " + languages.get(i).getCode());
                }
                return result;

            case Constants.MODE_TRANSLATE_TEXT:

                translation = translate.translate(objects[0]);
                return new String[] { translation.getTranslatedText() };

            case Constants.MODE_TRANSLATE_OPTIONS:

                srcLang = Translate.TranslateOption.sourceLanguage(objects[0]);
                tgtLang = Translate.TranslateOption.targetLanguage(objects[1]);

                translation = translate.translate(objects[0], srcLang, tgtLang);
                return new String[] { translation.getTranslatedText() };

            case Constants.MODE_TRANSLATE_OPTIONS_MODE:

                srcLang = Translate.TranslateOption.sourceLanguage(objects[0]);
                tgtLang = Translate.TranslateOption.targetLanguage(objects[1]);
                Translate.TranslateOption model = Translate.TranslateOption.model(Boolean.parseBoolean(objects[2]) ?
                        Constants.TRANSLATION_MODEL_NMT :
                        Constants.TRANSLATION_MODEL_BASE);

                translation = translate.translate(objects[0], srcLang, tgtLang, model);
                return new String[]{ translation.getTranslatedText() };


                default:
                    return null;
        }
    }

    @Override
    protected void onPostExecute(String[] o)
    {
        callbackAsyncTask.onAsyncTaskDone(o);
        super.onPostExecute(o);
    }
}
