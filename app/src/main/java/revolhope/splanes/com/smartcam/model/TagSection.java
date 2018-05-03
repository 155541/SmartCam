package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.UUID;

import revolhope.splanes.com.smartcam.helper.converters.DrawableRoomConverter;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "table_tag_section",
        indices = {@Index(value = {"tag_section_name"}, unique = true), @Index(value = {"tag_section_icon_id"})},
        foreignKeys = @ForeignKey(entity = Icon.class, parentColumns = "icon_id",
        childColumns = "tag_section_icon_id", onDelete = CASCADE, onUpdate = CASCADE))
public class TagSection {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "tag_section_id")
    private String tagSectionId;

    @NonNull
    @ColumnInfo(name = "tag_section_name")
    private String tagSectionName;

    @Nullable
    @ColumnInfo(name = "tag_section_icon_id")
    private String tagSectionIconId;

    public TagSection(@NonNull String tagSectionId, @NonNull String tagSectionName, @Nullable String tagSectionIconId)
    {
        this.tagSectionId = tagSectionId;
        this.tagSectionName = tagSectionName;
        this.tagSectionIconId = tagSectionIconId;
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

    @Nullable
    public String getTagSectionIconId() {
        return tagSectionIconId;
    }

    public void setTagSectionIconId(@Nullable String tagSectionIconId) {
        this.tagSectionIconId = tagSectionIconId;
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
