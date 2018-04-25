package revolhope.splanes.com.smartcam.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "table_tag", 
        indices = {@Index(value = {"tag_name"}, unique = true)})
public class Tag {
    
    @PrimaryKey
    @ColumnInfo(name = "tag_id")
    private String tagId;
    
    @ColumnInfo(name = "tag_name")
    private String tagName;

    @Ignore
    public Tag(String tagName)
    {
        this.tagId = UUID.randomUUID().toString();
        this.tagName = tagName;
    }
        
    public Tag(String tagId, String tagName)
    {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
