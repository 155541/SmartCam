package revolhope.splanes.com.smartcam.database;


@Database(entities = {Card.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    public abstract CardDao cardDao();
    //public abstract NotesDao notesDao();
    
}
