package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;


@Entity(tableName = "table_icon")
public class Icon
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "icon_id")
    private String iconId;

    @NonNull
    @ColumnInfo(name = "icon_drawable_id")
    private Integer iconDrawableId;

    @Ignore
    public Icon(@NonNull Integer iconDrawableId)
    {
        this.iconId = UUID.randomUUID().toString();
        this.iconDrawableId = iconDrawableId;
    }

    public Icon(@NonNull String iconId, @NonNull Integer iconDrawableId)
    {
        this.iconId = iconId;
        this.iconDrawableId = iconDrawableId;
    }

    @NonNull
    public String getIconId() {
        return iconId;
    }

    public void setIconId(@NonNull String iconId) {
        this.iconId = iconId;
    }

    @NonNull
    public Integer getIconDrawableId() {
        return iconDrawableId;
    }

    public void setIconDrawableId(@NonNull Integer iconDrawableId) {
        this.iconDrawableId = iconDrawableId;
    }
}
