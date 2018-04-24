package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import revolhope.splanes.com.smartcam.model.Contact;

@Dao
public interface CardDao {

    @Query("SELECT * FROM Contact")
    List<Contact> getAll();

    @Query("SELECT * FROM Contact WHERE cardId IN (:cardIds)")
    List<Contact> loadAllByIds(int[] cardIds);

    @Query("SELECT * FROM Contact WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    Contact findByName(String first, String last);

    @Insert
    void insertAll(Contact... contacts);

    @Delete
    void delete(Contact contact);
    
    @Delete
    void deleteAll(Contact... contacts);
}
