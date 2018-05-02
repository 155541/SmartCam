package revolhope.splanes.com.smartcam.model.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import revolhope.splanes.com.smartcam.database.AppRepository;
import revolhope.splanes.com.smartcam.model.TagSection;

public class TagSectionViewModel extends AndroidViewModel
{

    private AppRepository mRepository;
    private LiveData<List<TagSection>> mAllTagSections;

    public TagSectionViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllTagSections = mRepository.getAllTagSections();
    }

    public LiveData<List<TagSection>> getAllTagSections() { return mAllTagSections; }

    public void insertTagSection(TagSection... tagSections) { mRepository.insertTagSection(tagSections); }
}
