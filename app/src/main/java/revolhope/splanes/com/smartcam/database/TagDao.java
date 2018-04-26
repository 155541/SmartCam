package revolhope.splanes.com.smartcam.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Tag;

@Dao
public interface TagDao
{

    @Query("SELECT * from " + Constants.TABLE_TAG +" ORDER BY tag_name ASC")
    LiveData<List<Tag>> getAll();

    @Insert
    void insert(Tag... tags);

    @Delete
    void delete(Tag... tags);
}
