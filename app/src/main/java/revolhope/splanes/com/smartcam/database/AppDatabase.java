package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.Note;
import revolhope.splanes.com.smartcam.model.Tag;

/* =================================================================================
 * DOCUMENTATION:
 * https://www.pluralsight.com/guides/android/making-a-notes-app-using-room-database
 *
 * ---> https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0 <---
 *
 * Schema:
 * https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we
 * =============================================================================== */

@Database(entities = {Contact.class, Tag.class, Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase INSTANCE;
    
    public abstract ContactDao contactDao();
    public abstract TagDao tagDao();
    //public abstract NotesDao notesDao();
        
    public static AppDatabase getInstance(final Context context)
    { 
        if (null == INSTANCE)
        {
            synchronized (AppDatabase.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = buildDatabaseInstance(context);
                }
            }
        } 
        return INSTANCE;
    }
    
    @NonNull
    private static AppDatabase buildDatabaseInstance(@NonNull Context context)
    { 
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Constants.DB_NAME).build();
    }
}
