package revolhope.splanes.com.smartcam.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagSection;

public class AppRepository {

    private TagDao mTagDao;
    private TagSectionDao mTagSectionDao;
    private ContactDao mContactDao;
    //private NoteDao mNoteDao;


    private LiveData<List<Tag>> mAllTags;
    private LiveData<List<TagSection>> mAllTagSections;


    private LiveData<List<Contact>> mAllContacts;
    //private LiveData<List<Note>> mAllNotes;

    public AppRepository(Application app)
    {
        AppDatabase appDatabase = AppDatabase.getInstance(app);

        mContactDao = appDatabase.contactDao();
        mTagDao = appDatabase.tagDao();
        mTagSectionDao = appDatabase.tagSectionDao();
        //mNoteDao = appDatabase.noteDao();

        mAllContacts = mContactDao.getAll();
        mAllTags = mTagDao.getAll();
        mAllTagSections = mTagSectionDao.getAll();
        //mAllNotes = mNoteDao.getAll();
    }


    LiveData<List<Contact>> getAllContacts()
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

//    LiveData<List<Note>> getAllNotes() {
//        return mAllNotes;
//    }

    public void insertTag (Tag... tags)
    {
        new insertTagAsyncTask(mTagDao).execute(tags);
    }

    public void insertTagSection(TagSection... tagSections)
    {
        new insertTagSectionAsyncTask(mTagSectionDao).execute(tagSections);
    }

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
}
