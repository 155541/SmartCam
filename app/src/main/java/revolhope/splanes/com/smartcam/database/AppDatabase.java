package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.Note;

/* =================================================================================
 * DOCUMENTATION:
 * https://www.pluralsight.com/guides/android/making-a-notes-app-using-room-database
 *
 * ---> https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0 <---
 *
 * Schema:
 * https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we
 * =============================================================================== */

@Database(entities = {Contact.class, Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase appDatabase;
    
    public abstract ContactDao contactDao();
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
