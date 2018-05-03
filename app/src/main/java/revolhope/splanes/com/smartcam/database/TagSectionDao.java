package revolhope.splanes.com.smartcam.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.TagSection;

@Dao
public interface TagSectionDao
{
    @Insert
    void insert(TagSection... tagSections);

    @Delete
    void delete(TagSection... tagSections);

    @Query("SELECT * FROM " + Constants.TABLE_TAG_SECTION +" ORDER BY tag_section_name ASC")
    LiveData<List<TagSection>> getAll();

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_TAG_SECTION +" WHERE tag_section_id LIKE :tagSectionId LIMIT 1")
    int count(String tagSectionId);

}
