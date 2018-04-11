package revolhope.splanes.com.smartcam.database;

@Dao
public interface CardDao {

    @Query("SELECT * FROM card")
    List<User> getAll();

    @Query("SELECT * FROM card WHERE cardId IN (:Ids)")
    List<User> loadAllByIds(int[] cardIds);

    @Query("SELECT * FROM card WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insertAll(Card... cards);

    @Delete
    void delete(Card card);
}
