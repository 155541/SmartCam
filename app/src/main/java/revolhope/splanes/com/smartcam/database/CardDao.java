package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import revolhope.splanes.com.smartcam.model.Card;

@Dao
public interface CardDao {

    @Query("SELECT * FROM card")
    List<Card> getAll();

    @Query("SELECT * FROM card WHERE cardId IN (:cardIds)")
    List<Card> loadAllByIds(int[] cardIds);

    @Query("SELECT * FROM card WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    Card findByName(String first, String last);

    @Insert
    void insertAll(Card... cards);

    @Delete
    void delete(Card card);
}
