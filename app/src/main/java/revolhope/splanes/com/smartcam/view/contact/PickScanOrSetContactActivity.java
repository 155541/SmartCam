package revolhope.splanes.com.smartcam.view.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import revolhope.splanes.com.smartcam.R;

public class PickScanOrSetContactActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_pick_scan_set_contact);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle("");
        }

        findViewById(R.id.fabManually).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), PreContactManuallyActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.fabScan).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), PreviewCamContactActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
