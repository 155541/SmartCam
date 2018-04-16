package revolhope.splanes.com.smartcam.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.helper.translate.CallbackAsyncTask;
import revolhope.splanes.com.smartcam.helper.translate.TranslateAsyncTask;

public class TranslateActivity extends AppCompatActivity {

    private TranslateAsyncTask asyncTask;
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

        requestInternetPermission();

        final TextView fromLang = findViewById(R.id.textView_language_from);
        TextView toLang = findViewById(R.id.textView_language_to);

        fromLang.setText(R.string.prompt_detect_lang);
        toLang.setText(R.string.prompt_spanish_lang);

        final EditText textToTranslate = findViewById(R.id.editText_to_translate);
        TextView textTranslated = findViewById(R.id.textView_translatedText);

        textToTranslate.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable)
            {
                final String str = editable.toString();
                if(str.isEmpty())
                {
                    fromLang.setText(R.string.prompt_language_no_detected);
                }
                else
                {
                    asyncTask = new TranslateAsyncTask(new CallbackAsyncTask()
                    {
                        @Override
                        public void onAsyncTaskDone(final String[] result)
                        {
                            final int size = result.length;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(size > 1)
                                    {
                                        fromLang.setText(R.string.prompt_pick_detected_languages);
                                        fromLang.setOnClickListener(new View.OnClickListener()
                                        {
                                            int selected = -1;

                                            @Override
                                            public void onClick(View view) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                                final String[] lang = Constants.getFormattedLang(result);

                                                builder .setTitle("Pick language")
                                                        .setSingleChoiceItems(lang, -1, new DialogInterface.OnClickListener()
                                                        {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i)
                                                            {
                                                                selected = i;
                                                            }
                                                        })
                                                        .setPositiveButton("Pick", new DialogInterface.OnClickListener()
                                                        {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i)
                                                            {
                                                                if(selected != -1)
                                                                    fromLang.setText(Constants.mapLanguages.get(lang[selected]));
                                                                else
                                                                    fromLang.setText(R.string.prompt_language_no_detected);
                                                            }
                                                        })
                                                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                                                        {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i)
                                                            {
                                                                fromLang.setText(R.string.prompt_language_no_detected);
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                    else if( size == 1)
                                    {
                                        fromLang.setText(Constants.mapLanguages.get(result[0]));
                                    }
                                    else
                                    {
                                        fromLang.setText(R.string.prompt_language_no_detected);
                                    }
                                }
                            });
                        }
                    }, Constants.MODE_TRANSLATE_DETECT);
                    asyncTask.execute(str);
                }
            }
        });

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
                    asyncTask = new TranslateAsyncTask(new CallbackAsyncTask()
                    {
                        @Override
                        public void onAsyncTaskDone(String[] result)
                        {
                            int size = result.length;
                            for (int i = 0 ; i < size ; i++)
                            {
                                System.out.println(" :......: ASYNC-TASK RESULT :......: Name:" + result[i]);
                            }

                        }

                    }, Constants.MODE_TRANSLATE_SUPPORTED_LANG);
                    asyncTask.execute("ca");
                }
            }
        });
    }


    private void requestInternetPermission()
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
