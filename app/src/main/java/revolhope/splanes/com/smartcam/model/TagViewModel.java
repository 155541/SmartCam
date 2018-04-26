package revolhope.splanes.com.smartcam.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;


import java.util.List;

import revolhope.splanes.com.smartcam.database.AppRepository;

public class TagViewModel extends AndroidViewModel
{

    private AppRepository mRepository;
    private LiveData<List<Tag>> mAllTags;

    public TagViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllTags = mRepository.getAllTags();
    }

    public LiveData<List<Tag>> getAllTags() { return mAllTags; }

    public void insertTag(Tag... tags) { mRepository.insertTag(tags); }
}
