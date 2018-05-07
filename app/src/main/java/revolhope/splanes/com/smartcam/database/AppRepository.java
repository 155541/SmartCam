package revolhope.splanes.com.smartcam.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.Icon;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagSection;

public class AppRepository {

    private TagDao mTagDao;
    private TagSectionDao mTagSectionDao;
    private ContactDao mContactDao;
    private IconDao mIconDao;

    private LiveData<List<Tag>> mAllTags;
    private LiveData<List<TagSection>> mAllTagSections;
    private LiveData<List<Icon>> mAllIcons;
    private LiveData<List<Contact>> mAllContacts;

// =================================================================================================
//                                        CONSTRUCTOR
// =================================================================================================

    public AppRepository(Application app)
    {
        AppDatabase appDatabase = AppDatabase.getInstance(app);

        mContactDao = appDatabase.contactDao();
        mTagDao = appDatabase.tagDao();
        mTagSectionDao = appDatabase.tagSectionDao();
        mIconDao = appDatabase.iconDao();

        mAllContacts = mContactDao.getAll();
        mAllTags = mTagDao.getAll();
        mAllTagSections = mTagSectionDao.getAll();
        mAllIcons = mIconDao.getAll();
    }


// =================================================================================================
//                                        SELECTS CALL
// =================================================================================================

    public LiveData<List<Contact>> getAllContacts()
    {
        return mAllContacts;
    }

    public LiveData<List<Tag>> getAllTags()
    {
        return mAllTags;
    }

    public LiveData<List<TagSection>> getAllTagSections()
    {
        return mAllTagSections;
    }

    public LiveData<List<Icon>> getAllIcons() { return mAllIcons; }


// =================================================================================================
//                                        INSERT CALL
// =================================================================================================

    public void insertTag (Tag... tags)
    {
        new insertTagAsyncTask(mTagDao).execute(tags);
    }

    public void insertTagSection(TagSection... tagSections)
    {
        new insertTagSectionAsyncTask(mTagSectionDao).execute(tagSections);
    }

    public void insertIcon(Icon... icons)
    {
        new insertIconAsynTask(mIconDao).execute(icons);
    }


// =================================================================================================
//                                      UPDATE CALL
// =================================================================================================

    public void updateTagSection(TagSection... tagSections)
    {
        new updateTagSectionAsyncTask(mTagSectionDao).execute();
    }

// =================================================================================================
//                            INSERTS ASYNC TASK IMPLEMENTATION
// =================================================================================================

    private static class insertTagAsyncTask extends AsyncTask<Tag, Void, Boolean>
    {
        private TagDao mTagDao;
        private insertTagAsyncTask(TagDao mTagDao) {
            this.mTagDao = mTagDao;
        }

        @Override
        protected Boolean doInBackground(Tag... tags) {
            if(mTagDao != null)
            {
                boolean b = true;
                for (Tag tag : tags)
                {
                    if (mTagDao.count(tag.getTagId()) == 1) b = false;
                }
                if (b)
                {
                    mTagDao.insert(tags);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            // DO ANYTHING WITH TRUE OR FALSE
        }
    }

    private static class insertTagSectionAsyncTask extends AsyncTask<TagSection, Void, Void>
    {
        private TagSectionDao mTagSectionDao;

        private insertTagSectionAsyncTask(TagSectionDao mTagSectionDao) {
            this.mTagSectionDao = mTagSectionDao;
        }

        @Override
        protected Void doInBackground(TagSection... tagSections) {
            if(mTagSectionDao!= null)
            {
                mTagSectionDao.insert(tagSections);
            }
            return null;
        }
    }

    private static class insertIconAsynTask extends AsyncTask<Icon, Void, Void>
    {
        private IconDao mIconDao;

        private insertIconAsynTask(IconDao mIconDao)
        {
            this.mIconDao = mIconDao;
        }

        @Override
        protected Void doInBackground(Icon... icons) {
            if (mIconDao != null)
            {
                mIconDao.insert(icons);
            }
            return null;
        }
    }


// =================================================================================================
//                              UPDATE ASYNC TASK IMPLEMENTATION
// =================================================================================================

    private static class updateTagSectionAsyncTask extends AsyncTask<TagSection, Void, Void>
    {
        TagSectionDao mTagSectionDao;

        private updateTagSectionAsyncTask(TagSectionDao mTagSectionDao)
        {
            this.mTagSectionDao = mTagSectionDao;
        }

        @Override
        protected Void doInBackground(TagSection... tagSections) {

            if (mTagSectionDao != null)
            {
                int i = mTagSectionDao.update(tagSections);
                System.out.println(" :......: ROWs UPDATED :......: " + i);
            }
            return null;
        }
    }
}
