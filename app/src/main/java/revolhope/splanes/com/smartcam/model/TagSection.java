package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.UUID;

@Entity(tableName = "table_tag_section",
        indices = {@Index(value = {"tagSectionName"}, unique = true)})
public class TagSection {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "tagSectionId")
    private String tagSectionId;

    @NonNull
    @ColumnInfo(name = "tagSectionName")
    private String tagSectionName;


    public TagSection(@NonNull String tagSectionId, @NonNull String tagSectionName)
    {
        this.tagSectionId = tagSectionId;
        this.tagSectionName = tagSectionName;
    }

    @Ignore
    public TagSection(@NonNull String tagSectionName)
    {
        this.tagSectionId = UUID.randomUUID().toString();
        this.tagSectionName = tagSectionName;
    }

    @NonNull
    public String getTagSectionId()
    {
        return tagSectionId;
    }

    public void setTagSectionId(@NonNull String tagSectionId) {
        this.tagSectionId = tagSectionId;
    }

    @NonNull
    public String getTagSectionName() {
        return tagSectionName;
    }

    public void setTagSectionName(@NonNull String tagSectionName) {
        this.tagSectionName = tagSectionName;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj)
    {

        if (obj instanceof TagSection)
        {
            return this.tagSectionId.equals(((TagSection) obj).tagSectionId);
        }
        else
        {
            return false;
        }
    }
}
