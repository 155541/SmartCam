package revolhope.splanes.com.smartcam.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Icon;

@Dao
public interface IconDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Icon... icons);

    @Delete
    void delete(Icon... icons);

    @Query("SELECT * FROM " + Constants.TABLE_ICON +" ORDER BY icon_id ASC")
    LiveData<List<Icon>> getAll();

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_ICON +" WHERE icon_id LIKE :iconId LIMIT 1")
    int count(String iconId);
}
