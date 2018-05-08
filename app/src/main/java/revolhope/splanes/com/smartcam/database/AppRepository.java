package revolhope.splanes.com.smartcam.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.ContactTag;
import revolhope.splanes.com.smartcam.model.Icon;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagSection;

public class AppRepository {

    private TagDao mTagDao;
    private TagSectionDao mTagSectionDao;
    private ContactDao mContactDao;
    private IconDao mIconDao;
    private ContactTagDao mContactTagDao;

    private LiveData<List<Tag>> mAllTags;
    private LiveData<List<TagSection>> mAllTagSections;
    private LiveData<List<Icon>> mAllIcons;
    private LiveData<List<Contact>> mAllContacts;
    private LiveData<List<ContactTag>> mAllContactTags;

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
        mContactTagDao = appDatabase.contactTagDao();

        mAllContacts = mContactDao.getAll();
        mAllTags = mTagDao.getAll();
        mAllTagSections = mTagSectionDao.getAll();
        mAllIcons = mIconDao.getAll();
        mAllContactTags = mContactTagDao.getAll();
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

    public LiveData<List<ContactTag>> getAllContactTags() {return mAllContactTags; }


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
        new insertIconAsyncTask(mIconDao).execute(icons);
    }

    public void insertContactTag(ContactTag... contactTags)
    {
        new insertContactTagAsyncTask(mContactTagDao).execute(contactTags);
    }

    public void insertContact(Contact... contacts)
    {
        new insertContactAsyncTask(mContactDao).execute(contacts);
    }

// =================================================================================================
//                                      UPDATE CALL
// =================================================================================================

    public void updateTagSection(TagSection... tagSections)
    {
        new updateTagSectionAsyncTask(mTagSectionDao).execute(tagSections);
    }


// =================================================================================================
//                                      DELETE CALL
// =================================================================================================

    public void deleteTagSection(TagSection... tagSections)
    {
        new deleteTagSectionAsyncTask(mTagSectionDao).execute(tagSections);
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

    private static class insertIconAsyncTask extends AsyncTask<Icon, Void, Void>
    {
        private IconDao mIconDao;

        private insertIconAsyncTask(IconDao mIconDao)
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

    private static class insertContactTagAsyncTask extends AsyncTask<ContactTag, Void, Void>
    {
        private ContactTagDao mContactTagDao;

        private insertContactTagAsyncTask(ContactTagDao mContactTagDao)
        {
            this.mContactTagDao = mContactTagDao;
        }

        @Override
        protected Void doInBackground(ContactTag... contactTags)
        {
            if (mContactTagDao != null)
            {
                mContactTagDao.insert(contactTags);
            }
            return null;
        }
    }

    private static class insertContactAsyncTask extends AsyncTask<Contact, Void, Void>
    {

        private ContactDao mContactDao;

        public insertContactAsyncTask(ContactDao mContactDao)
        {
            this.mContactDao = mContactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {

            if (mContactDao != null)
            {
                mContactDao.insertAll(contacts);
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
                mTagSectionDao.update(tagSections);
            }
            return null;
        }
    }


// =================================================================================================
//                              DELETE ASYNC TASK IMPLEMENTATION
// =================================================================================================

    private static class deleteTagSectionAsyncTask extends AsyncTask<TagSection, Void, Void>
    {

        TagSectionDao mTagSectionDao;

        private deleteTagSectionAsyncTask(TagSectionDao mTagSectionDao)
        {
            this.mTagSectionDao = mTagSectionDao;
        }

        @Override
        protected Void doInBackground(TagSection... tagSections) {

            if (mTagSectionDao != null)
            {
                mTagSectionDao.delete(tagSections);
            }
            return null;
        }
    }
}
