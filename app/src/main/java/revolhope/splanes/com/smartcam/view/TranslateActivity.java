package revolhope.splanes.com.smartcam.view;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Optional;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.helper.translate.CallbackAsyncTask;
import revolhope.splanes.com.smartcam.helper.translate.TranslateAsyncTask;
import revolhope.splanes.com.smartcam.helper.translate.TranslatorProcessor;

public class TranslateActivity extends AppCompatActivity {

    private boolean permissionGranted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);



        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        requestInternedPermission();

        TextView fromLang = findViewById(R.id.textView_language_from);
        TextView toLang = findViewById(R.id.textView_language_to);

        fromLang.setText(R.string.prompt_detect_lang);
        toLang.setText(R.string.prompt_spanish_lang);

        final EditText textToTranslate = findViewById(R.id.editText_to_translate);
        TextView textTranslated = findViewById(R.id.textView_translatedText);

        Intent intent = getIntent();
        if(intent != null)
        {
            String str = intent.getStringExtra(Constants.TEXT_TO_TRANSLATE);
            textToTranslate.setText(str);
        }
        else
        {
            textToTranslate.setText(null);
        }


        textTranslated.setText(null);

        findViewById(R.id.button_translate).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(permissionGranted)
                {
                    TranslateAsyncTask asyncTask = new TranslateAsyncTask(new CallbackAsyncTask()
                    {
                        @Override
                        public void onAsyncTaskDone(String[] result)
                        {
                            System.out.println(" :......: ASYNC-TASK RESULT :......: " + result[0]);
                        }

                    }, Constants.MODE_TRANSLATE_DETECT);
                    asyncTask.execute("Hello");
                }
            }
        });
    }


    private void requestInternedPermission()
    {
       // Internet permission is not granted. Requesting permission
        final String[] permissions = new String[]{Manifest.permission.INTERNET};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET)) {
            ActivityCompat.requestPermissions(this, permissions, Constants.REQ_HANDLE_INTERNET_PERM);
            return;
        }

        ActivityCompat.requestPermissions(this, permissions, Constants.REQ_HANDLE_INTERNET_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode != Constants.REQ_HANDLE_INTERNET_PERM)
        {
            // Got unexpected permission result:  look requestCode
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            permissionGranted = true;
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        if( id == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
