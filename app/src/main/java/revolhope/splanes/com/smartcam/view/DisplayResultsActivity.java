package revolhope.splanes.com.smartcam.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import revolhope.splanes.com.smartcam.R;

public class DisplayResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scan_result);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(getIntent() != null && getIntent().hasExtra(PreviewCamActivity.TextRead))
        {
            EditText editText = findViewById(R.id.editText_Results);
            String scanResult = getIntent().getStringExtra(PreviewCamActivity.TextRead);
            editText.setText(scanResult);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
