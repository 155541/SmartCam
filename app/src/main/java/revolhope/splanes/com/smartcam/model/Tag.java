package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/*
 * En el cas que el 'unique constrain' no funciones, provar amb @NonNull sobre els atributs unique (solucio d'un codelab Google)
 */
@Entity(tableName = "table_tag",
        indices = {@Index(value = {"tag_name"}, unique = true), @Index(value = {"tag_section_id"})},
        foreignKeys = @ForeignKey(entity = TagSection.class, parentColumns = "tag_section_id",
                childColumns = "tag_section_id", onDelete = CASCADE, onUpdate = CASCADE))
public class Tag {
    
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "tag_id")
    private String tagId;

    @NonNull
    @ColumnInfo(name = "tag_name")
    private String tagName;

    @NonNull
    @ColumnInfo(name = "tag_section_id")
    private String tagSectionId;

    @Ignore
    public Tag(@NonNull String tagName, @NonNull String tagSectionId)
    {
        this.tagId = UUID.randomUUID().toString();
        this.tagName = tagName;
        this.tagSectionId = tagSectionId;
    }
        
    public Tag(@NonNull String tagId, @NonNull String tagName, @NonNull String tagSectionId)
    {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagSectionId = tagSectionId;
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

    @NonNull
    public String getTagSectionId() {
        return tagSectionId;
    }

    public void setTagSectionId(@NonNull String tagSectionId) {
        this.tagSectionId = tagSectionId;
    }
}
