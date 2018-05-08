package revolhope.splanes.com.smartcam.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Tag;

@Dao
public interface TagDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tag... tags);

    @Delete
    void delete(Tag... tags);
    
    @Query("SELECT * FROM " + Constants.TABLE_TAG +" ORDER BY tag_name ASC")
    LiveData<List<Tag>> getAll();
    
    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_TAG +" WHERE tag_id LIKE :tagId LIMIT 1")
    int count(String tagId);

    @Query("SELECT * FROM " + Constants.TABLE_TAG + " WHERE tag_section_id LIKE :tagSectionId ORDER BY tag_name ASC")
    LiveData<List<Tag>> getTagsFromSection(String tagSectionId);
}
