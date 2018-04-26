package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

/*
 * En el cas que el 'unique constrain' no funciones, provar amb @NonNull sobre els atributs unique (solucio d'un codelab Google)
 */
@Entity(tableName = "table_tag")
public class Tag {
    
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "tag_id")
    private String tagId;

    @NonNull
    @ColumnInfo(name = "tag_name")
    private String tagName;

    @Ignore
    public Tag(@NonNull String tagName)
    {
        this.tagId = UUID.randomUUID().toString();
        this.tagName = tagName;
    }
        
    public Tag(@NonNull String tagId, @NonNull String tagName)
    {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    @NonNull
    public String getTagId() {
        return tagId;
    }

    public void setTagId(@NonNull String tagId) {
        this.tagId = tagId;
    }

    @NonNull
    public String getTagName() {
        return tagName;
    }

    public void setTagName(@NonNull String tagName) {
        this.tagName = tagName;
    }
}
