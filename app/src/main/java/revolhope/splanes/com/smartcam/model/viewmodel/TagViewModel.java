package revolhope.splanes.com.smartcam.model.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;


import java.util.List;
import java.util.Map;

import revolhope.splanes.com.smartcam.database.AppRepository;
import revolhope.splanes.com.smartcam.model.Tag;

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
