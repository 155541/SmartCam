package revolhope.splanes.com.smartcam.model;

@Entity(tableName = "table_tag", 
        indices = {@Index(value = {"tag_name"},
        unique = true)})
public class Tag {
    
    @PrimaryKey
    @ColumnInfo(name = "tag_id")
    private int tagId;
    
    @ColumnInfo(name = "tag_name")
    private String tagName;

    public Tag(int tagId, String tagName)
    {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
