package revolhope.splanes.com.smartcam.helper.translate;

import java.io.Serializable;

public interface CallbackPickLang extends Serializable{
    void onLangPicked(String result, int mode);
}
