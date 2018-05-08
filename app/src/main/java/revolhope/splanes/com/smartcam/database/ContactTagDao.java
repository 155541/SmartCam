package revolhope.splanes.com.smartcam.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.ContactTag;

@Dao
public interface ContactTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ContactTag... contactTags);

    @Delete
    void delete(ContactTag... contactTags);

    @Query("SELECT * FROM " + Constants.TABLE_CONTACT_TAG +" ORDER BY contact_id ASC")
    LiveData<List<ContactTag>> getAll();

    @Query("SELECT * FROM " + Constants.TABLE_CONTACT_TAG + " WHERE contact_id LIKE :contactId ORDER BY tag_id ASC")
    LiveData<List<ContactTag>> get(String contactId);

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_CONTACT_TAG +" WHERE (contact_id LIKE :contactId AND tag_id LIKE :tagId) LIMIT 1")
    int count(String contactId, String tagId);

}
