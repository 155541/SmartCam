package revolhope.splanes.com.smartcam.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.Tag;

public class AppRepository {

    private TagDao mTagDao;
    private ContactDao mContactDao;
    //private NoteDao mNoteDao;


    private LiveData<List<Tag>> mAllTags;
    private LiveData<List<Contact>> mAllContacts;
    //private LiveData<List<Note>> mAllNotes;

    public AppRepository(Application app)
    {
        AppDatabase appDatabase = AppDatabase.getInstance(app);

        mContactDao = appDatabase.contactDao();
        mTagDao = appDatabase.tagDao();
        //mNoteDao = appDatabase.noteDao();

        mAllContacts = mContactDao.getAll();
        mAllTags = mTagDao.getAll();
        //mAllNotes = mNoteDao.getAll();
    }


    LiveData<List<Contact>> getAllContacts() {
        return mAllContacts;
    }

    public LiveData<List<Tag>> getAllTags() {
        return mAllTags;
    }

//    LiveData<List<Note>> getAllNotes() {
//        return mAllNotes;
//    }


    public void insertTag (Tag... tags) {
        new insertTagAsyncTask(mTagDao).execute(tags);
    }

    private static class insertTagAsyncTask extends AsyncTask<Tag, Void, Void>
    {
        private TagDao mTagDao;

        private insertTagAsyncTask(TagDao mTagDao) {
            this.mTagDao = mTagDao;
        }

        @Override
        protected Void doInBackground(Tag... tags) {
            if(mTagDao != null)
            {
                mTagDao.insert(tags);
            }
            return null;
        }
    }
}
