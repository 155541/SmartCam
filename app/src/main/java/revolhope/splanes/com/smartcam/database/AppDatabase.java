package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
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
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Constants.DB_NAME)
            .addCallback(dbCallback)
            .build();
    }
    
    private static RoomDatabase.Callback dbCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            if(INSTANCE != null)
            {
                new PopulateDatabaseAsync(INSTANCE).execute();
            }
        }   
    };
    
    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void>
    {
        private TagDao mTagDao; //I supose "final" no will work
        private final Tag[] predeterminedTags =  {
                new Tag("Restaurant"),
                new Tag("Bar"),
                new Tag("Cheap"),
                new Tag("Expensive"),
                new Tag("Romantic"),
                new Tag("Asia"),
                new Tag("Sushi"),
                new Tag("BBQ"),
                new Tag("Hamburger")
        };
        
        private PopulateDatabaseAsync(AppDatabase appDatabase)
        {
            mTagDao = appDatabase.tagDao();
        }

        @Override
        protected Void doInBackground(final Void... params)
        {
            int size = predeterminedTags.length;
            
            for (Tag tag : predeterminedTags)
            {
                if(mTagDao.exists(tag.getTagId()) == null)
                {
                    mTagDao.insert(tag);
                }
            }
            return null;
        }
    }
}
