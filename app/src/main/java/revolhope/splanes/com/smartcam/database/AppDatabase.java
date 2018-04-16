package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Card;
import revolhope.splanes.com.smartcam.model.TranslationLog;

/* =================================================================================
 * DOCUMENTATION:
 * https://www.pluralsight.com/guides/android/making-a-notes-app-using-room-database
 *
 * Schema:
 * https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we
 * =============================================================================== */

@Database(entities = {Card.class, TranslationLog.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase appDatabase;
    
    public abstract CardDao cardDao();
    public abstract TranslationLogDao translationLogDao();
    //public abstract NotesDao notesDao();
        
    public static AppDatabase getInstance(Context context)
    { 
        if (null == appDatabase) 
        { 
            appDatabase = buildDatabaseInstance(context); 
        } 
        return appDatabase;
    }
    
    private static AppDatabase buildDatabaseInstance(Context context)
    { 
        return Room.databaseBuilder(context, AppDatabase.class, Constants.DB_NAME).build();
    }
}
