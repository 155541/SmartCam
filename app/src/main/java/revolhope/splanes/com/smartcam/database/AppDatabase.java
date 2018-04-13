package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import revolhope.splanes.com.smartcam.model.Card;

/* =================================================================================
 * DOCUMENTATION:
 *
 * https://www.pluralsight.com/guides/android/making-a-notes-app-using-room-database
 * =============================================================================== */

@Database(entities = {Card.class, TranslationLog.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    public abstract CardDao cardDao();
    public abstract TranslationLogDao translationLogDao();
    //public abstract NotesDao notesDao();
    
}
