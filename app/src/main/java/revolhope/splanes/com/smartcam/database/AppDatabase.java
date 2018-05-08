package revolhope.splanes.com.smartcam.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.ContactTag;
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

@Database(entities = {Contact.class, Tag.class, Note.class, TagSection.class, Icon.class, ContactTag.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase INSTANCE;

    public abstract ContactDao contactDao();
    public abstract TagDao tagDao();
    public abstract TagSectionDao tagSectionDao();
    public abstract IconDao iconDao();
    public abstract ContactTagDao contactTagDao();
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
                    new Icon("predeterminedIconType", R.drawable.ic_local_bar_black_24dp_small),
                    new Icon("predeterminedIconEconomic", R.drawable.ic_monetization_on_black_24dp),
                    new Icon("predeterminedIconCountry", R.drawable.ic_public_black_24dp_small),
                    new Icon("predeterminedIconSpeed", R.drawable.ic_timer_black_24dp),
                    new Icon("predeterminedIconSpeciality", R.drawable.ic_star_black_24dp),
                    new Icon("predeterminedIconFriendly", R.drawable.ic_people_black_24dp)
            };

            predeterminedTagSections = new TagSection[] {
                    new TagSection("predeterminedSectionType", "Type", "predeterminedIconType"),         // 0
                    new TagSection("predeterminedSectionEconomic","Economic", "predeterminedIconEconomic"),     // 1
                    new TagSection("predeterminedSectionCountry", "Country", "predeterminedIconCountry"),      // 2
                    new TagSection("predeterminedSectionSpeed", "Speed", "predeterminedIconSpeed"),        // 3
                    new TagSection("predeterminedSectionSpeciality","Speciality", "predeterminedIconSpeciality"),   // 4
                    new TagSection("predeterminedSectionFriendly", "Friendly", "predeterminedIconFriendly")      // 5
            };

            predeterminedTags = new Tag[] {
                    new Tag("predeterminedTagRestaurant", "Restaurant", "predeterminedSectionType"),
                    new Tag("predeterminedTagBar", "Bar", "predeterminedSectionType"),
                    new Tag("predeterminedTagCheap", "Cheap", "predeterminedSectionEconomic"),
                    new Tag("predeterminedTagExpensive", "Expensive", "predeterminedSectionEconomic"),
                    new Tag("predeterminedTagJapanese", "Japanese", "predeterminedSectionCountry"),
                    new Tag("predeterminedTagUSA", "USA", "predeterminedSectionCountry"),
                    new Tag("predeterminedTagFrench", "French", "predeterminedSectionCountry"),
                    new Tag("predeterminedTagItalian", "Italian", "predeterminedSectionCountry"),
                    new Tag("predeterminedTagMexican", "Mexican", "predeterminedSectionCountry"),
                    new Tag("predeterminedTagChinese", "Chinese", "predeterminedSectionCountry"),
                    new Tag("predeterminedTagFastFood", "Fast food", "predeterminedSectionSpeed"),
                    new Tag("predeterminedTagFast", "Fast", "predeterminedSectionSpeed"),
                    new Tag("predeterminedTagMedium", "Medium", "predeterminedSectionSpeed"),
                    new Tag("predeterminedTagSlow", "Slow", "predeterminedSectionSpeed"),
                    new Tag("predeterminedTagSushi", "Sushi", "predeterminedSectionSpeciality"),
                    new Tag("predeterminedTagBBQ", "BBQ", "predeterminedSectionSpeciality"),
                    new Tag("predeterminedTagHamburger", "Hamburger", "predeterminedSectionSpeciality"),
                    new Tag("predeterminedTagRomantic", "Romantic", "predeterminedSectionFriendly"),
                    new Tag("predeterminedTagFamiliar", "Familiar", "predeterminedSectionFriendly"),
                    new Tag("predeterminedTagGroup", "Group", "predeterminedSectionFriendly"),
                    new Tag("predeterminedTagChildren", "Children", "predeterminedSectionFriendly")
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
