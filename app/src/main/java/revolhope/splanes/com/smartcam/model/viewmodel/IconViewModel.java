package revolhope.splanes.com.smartcam.model.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import revolhope.splanes.com.smartcam.database.AppRepository;
import revolhope.splanes.com.smartcam.model.Icon;

public class IconViewModel extends AndroidViewModel {

    private AppRepository mRepository;
    private LiveData<List<Icon>> mAllIcons;

    public IconViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllIcons = mRepository.getAllIcons();
    }

    public LiveData<List<Icon>> getAllIcons() { return mAllIcons; }

    public void insertIcon(Icon... icons) { mRepository.insertIcon(icons); }

}
