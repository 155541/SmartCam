package revolhope.splanes.com.smartcam.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;

public class DisplayResultsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scan_result);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(R.string.result_activity_subtitle);
        }

        final EditText editText = findViewById(R.id.editText_Results);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.TEXTREAD))
        {
            String result = getIntent().getStringExtra(Constants.TEXTREAD);
            editText.setText(result);
        }

        findViewById(R.id.textView_translate).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String text = editText.getText().toString();
                if(!text.isEmpty())
                {
                    Intent i = new Intent(getApplicationContext(), TranslateActivity.class);
                    i.putExtra(Constants.TEXT_TO_TRANSLATE, text);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "There is no text to translate..", Toast.LENGTH_LONG).show();
                }
            }
        });
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
