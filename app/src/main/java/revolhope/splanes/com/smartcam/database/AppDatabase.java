package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.Icon;
import revolhope.splanes.com.smartcam.model.Note;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagSection;

/* =================================================================================
 * DOCUMENTATION:
 * https://www.pluralsight.com/guides/android/making-a-notes-app-using-room-database
 *
 * ---> https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0 <---
 * =============================================================================== */

@Database(entities = {Contact.class, Tag.class, Note.class, TagSection.class, Icon.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase INSTANCE;

    public abstract ContactDao contactDao();
    public abstract TagDao tagDao();
    public abstract TagSectionDao tagSectionDao();
    public abstract IconDao iconDao();
    //public abstract NotesDao notesDao();


    static AppDatabase getInstance(final Context context)
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
        private TagDao mTagDao;
        private TagSectionDao mTagSectionDao;
        private IconDao mIconDao;
        private Icon[] predeterminedIcons;
        private TagSection[] predeterminedTagSections;
        private Tag[] predeterminedTags;
        
        private PopulateDatabaseAsync(@NonNull AppDatabase appDatabase)
        {
            mTagDao = appDatabase.tagDao();
            mTagSectionDao = appDatabase.tagSectionDao();
            mIconDao = appDatabase.iconDao();

            predeterminedIcons = new Icon[] {
                    new Icon(R.drawable.ic_local_bar_black_24dp_small),
                    new Icon(R.drawable.ic_monetization_on_black_24dp),
                    new Icon(R.drawable.ic_public_black_24dp_small),
                    new Icon(R.drawable.ic_timer_black_24dp),
                    new Icon(R.drawable.ic_star_black_24dp),
                    new Icon(R.drawable.ic_people_black_24dp)
            };

            predeterminedTagSections = new TagSection[] {
                    new TagSection("predeterminedSectionType", "Type", predeterminedIcons[0].getIconId()),         // 0
                    new TagSection("predeterminedSectionEconomic","Economic", predeterminedIcons[1].getIconId()),     // 1
                    new TagSection("predeterminedSectionCountry", "Country", predeterminedIcons[2].getIconId()),      // 2
                    new TagSection("predeterminedSectionSpeed", "Speed", predeterminedIcons[3].getIconId()),        // 3
                    new TagSection("predeterminedSectionSpeciality","Speciality", predeterminedIcons[4].getIconId()),   // 4
                    new TagSection("predeterminedSectionFriendly", "Friendly", predeterminedIcons[5].getIconId())      // 5
            };

            predeterminedTags = new Tag[] {
                    new Tag("predeterminedTagRestaurant", "Restaurant", predeterminedTagSections[0].getTagSectionId()),
                    new Tag("predeterminedTagBar", "Bar", predeterminedTagSections[0].getTagSectionId()),
                    new Tag("predeterminedTagCheap", "Cheap", predeterminedTagSections[1].getTagSectionId()),
                    new Tag("predeterminedTagExpensive", "Expensive", predeterminedTagSections[1].getTagSectionId()),
                    new Tag("predeterminedTagJapanese", "Japanese", predeterminedTagSections[2].getTagSectionId()),
                    new Tag("predeterminedTagUSA", "USA", predeterminedTagSections[2].getTagSectionId()),
                    new Tag("predeterminedTagFrench", "French", predeterminedTagSections[2].getTagSectionId()),
                    new Tag("predeterminedTagItalian", "Italian", predeterminedTagSections[2].getTagSectionId()),
                    new Tag("predeterminedTagMexican", "Mexican", predeterminedTagSections[2].getTagSectionId()),
                    new Tag("predeterminedTagChinese", "Chinese", predeterminedTagSections[2].getTagSectionId()),
                    new Tag("predeterminedTagFastFood", "Fast food", predeterminedTagSections[3].getTagSectionId()),
                    new Tag("predeterminedTagFast", "Fast", predeterminedTagSections[3].getTagSectionId()),
                    new Tag("predeterminedTagMedium", "Medium", predeterminedTagSections[3].getTagSectionId()),
                    new Tag("predeterminedTagSlow", "Slow", predeterminedTagSections[3].getTagSectionId()),
                    new Tag("predeterminedTagSushi", "Sushi", predeterminedTagSections[4].getTagSectionId()),
                    new Tag("predeterminedTagBBQ", "BBQ", predeterminedTagSections[4].getTagSectionId()),
                    new Tag("predeterminedTagHamburger", "Hamburger", predeterminedTagSections[4].getTagSectionId()),
                    new Tag("predeterminedTagRomantic", "Romantic", predeterminedTagSections[5].getTagSectionId()),
                    new Tag("predeterminedTagFamiliar", "Familiar", predeterminedTagSections[5].getTagSectionId()),
                    new Tag("predeterminedTagGroup", "Group", predeterminedTagSections[5].getTagSectionId()),
                    new Tag("predeterminedTagChildren", "Children", predeterminedTagSections[5].getTagSectionId())
            };
        }

        @Override
        protected Void doInBackground(final Void... params)
        {
            int count;

            for (Icon icon : predeterminedIcons)
            {
                count = mIconDao.count(icon.getIconId());
                if ( count == 0 )
                {
                    mIconDao.insert(icon);
                }
            }

            for(TagSection tagSection : predeterminedTagSections)
            {
                count = mTagSectionDao.count(tagSection.getTagSectionId());
                if (count == 0)
                {
                    mTagSectionDao.insert(tagSection);
                }
            }

            for (Tag tag : predeterminedTags)
            {
                count = mTagDao.count(tag.getTagId());
                if(count == 0)
                {
                    mTagDao.insert(tag);
                }
            }
            return null;
        }
    }
}
