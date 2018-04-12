package revolhope.splanes.com.smartcam.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import revolhope.splanes.com.smartcam.model.Card;

@Database(entities = {Card.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    public abstract CardDao cardDao();
    //public abstract NotesDao notesDao();
    
}