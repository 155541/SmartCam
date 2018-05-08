package revolhope.splanes.com.smartcam.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Contact;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM " + Constants.TABLE_CONTACT)
    LiveData<List<Contact>> getAll();

    @Query("SELECT * FROM "+ Constants.TABLE_CONTACT +" WHERE contact_id IN (:contactIds)")
    LiveData<List<Contact>> loadAllByIds(int[] contactIds);

    @Query("SELECT * FROM "+ Constants.TABLE_CONTACT + " WHERE contact_name LIKE :contactName LIMIT 1")
    LiveData<Contact> findByName(String contactName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Contact... contacts);

    @Delete
    void delete(Contact contact);
    
    @Delete
    void deleteAll(Contact... contacts);
}
