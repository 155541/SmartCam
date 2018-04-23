package revolhope.splanes.com.smartcam.view.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import revolhope.splanes.com.smartcam.R;

public class PreContactManuallyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_precontact);


        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null)
        {
            actionBar.setTitle("Enter new Contact (1/2)");
            actionBar.setSubtitle("Set information to store");
        }

    }
}
