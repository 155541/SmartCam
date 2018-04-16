package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TranslationLog {
    
    @PrimaryKey
    private int TranslationLogId;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "lang_source")
    private String srcLang;

    @ColumnInfo(name = "text_source")
    private String srcText;

    @ColumnInfo(name = "lang_target")
    private String trgLang;

    @ColumnInfo(name = "text_translated")
    private String trgText;

    public int getTranslationLogId() {
        return TranslationLogId;
    }

    public void setTranslationLogId(int translationLogId) {
        TranslationLogId = translationLogId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSrcLang() {
        return srcLang;
    }

    public void setSrcLang(String srcLang) {
        this.srcLang = srcLang;
    }

    public String getSrcText() {
        return srcText;
    }

    public void setSrcText(String srcText) {
        this.srcText = srcText;
    }

    public String getTrgLang() {
        return trgLang;
    }

    public void setTrgLang(String trgLang) {
        this.trgLang = trgLang;
    }

    public String getTrgText() {
        return trgText;
    }

    public void setTrgText(String trgText) {
        this.trgText = trgText;
    }
}
