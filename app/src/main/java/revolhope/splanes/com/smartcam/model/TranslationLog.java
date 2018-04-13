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
       
    // TODO: Create getters & setters. Are needed!
}
