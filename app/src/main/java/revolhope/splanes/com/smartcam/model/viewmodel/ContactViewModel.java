package revolhope.splanes.com.smartcam.model.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import revolhope.splanes.com.smartcam.database.AppRepository;
import revolhope.splanes.com.smartcam.model.Contact;

public class ContactViewModel extends AndroidViewModel
{

    private AppRepository mRepository;
    private LiveData<List<Contact>> mAllContact;

    public ContactViewModel (Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mAllContact = mRepository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() { return mAllContact; }

    public void insertContact(Contact... contacts) { mRepository.insertContact(contacts); }
}
