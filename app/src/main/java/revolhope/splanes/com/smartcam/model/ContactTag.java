package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "table_contact_tag",
        indices = {@Index(value = {"contact_id"}), @Index(value = {"tag_id"})},
        primaryKeys = {"contact_id","tag_id"},
        foreignKeys = {
            @ForeignKey(entity = Contact.class, parentColumns = "contact_id",
                childColumns = "contact_id", onDelete = CASCADE, onUpdate = CASCADE),
            @ForeignKey(entity = Tag.class, parentColumns = "tag_id",
                childColumns = "tag_id", onDelete = CASCADE, onUpdate = CASCADE)
        })
public class ContactTag
{
    @NonNull
    @ColumnInfo(name = "contact_id")
    private String contactId;

    @NonNull
    @ColumnInfo(name = "tag_id")
    private String tagId;

    public ContactTag(@NonNull String contactId, @NonNull String tagId)
    {
        this.contactId = contactId;
        this.tagId = tagId;
    }

    @NonNull
    public String getContactId()
    {
        return contactId;
    }

    public void setContactId(@NonNull String contactId)
    {
        this.contactId = contactId;
    }

    @NonNull
    public String getTagId()
    {
        return tagId;
    }

    public void setTagId(@NonNull String tagId)
    {
        this.tagId = tagId;
    }
}
