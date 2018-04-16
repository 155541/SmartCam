package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

import revolhope.splanes.com.smartcam.model.Card;
import revolhope.splanes.com.smartcam.model.TranslationLog;

@Dao
public interface TranslationLogDao {

    @Query("SELECT * FROM translationlog")
    List<TranslationLog> getAll();

    @Query("SELECT * FROM translationlog WHERE translationLogId IN (:translationLogIds)")
    List<TranslationLog> loadAllByIds(int[] translationLogIds);

//    @Query("SELECT * FROM card WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
//    Card findByName(String first, String last);

    @Insert
    void insertAll(Card... cards);

    @Delete
    void delete(Card card);
}
