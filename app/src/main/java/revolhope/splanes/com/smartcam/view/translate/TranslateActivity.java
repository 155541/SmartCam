package revolhope.splanes.com.smartcam.view.translate;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.helper.translate.CallbackAsyncTask;
import revolhope.splanes.com.smartcam.helper.translate.CallbackPickLang;
import revolhope.splanes.com.smartcam.helper.translate.TranslateAsyncTask;

public class TranslateActivity extends AppCompatActivity implements CallbackPickLang{

    private static final int PICK_LANG_FROM = 0;
    private static final int PICK_LANG_TO = 1;

    private TranslateAsyncTask asyncTask;
    private boolean permissionGranted;
    private CallbackPickLang callbackPickLang;

    private TextView fromLang;
    private TextView toLang;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        callbackPickLang = this;

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        requestInternetPermission();

        fromLang = findViewById(R.id.textView_language_from);
        toLang = findViewById(R.id.textView_language_to);

        fromLang.setText(R.string.prompt_no_language);
        fromLang.setTextColor(getColor(android.R.color.holo_red_dark));
        toLang.setText(R.string.prompt_spanish_lang);
        toLang.setTextColor(getColor(android.R.color.holo_blue_dark));

        final EditText textToTranslate = findViewById(R.id.editText_to_translate);
        final EditText textTranslated = findViewById(R.id.editText_translatedText);

        findViewById(R.id.button_detect_lang).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String str = textToTranslate.getText().toString();
                if(str.isEmpty())
                {
                    fromLang.setText(R.string.prompt_language_no_detected);
                    fromLang.setTextColor(getColor(android.R.color.holo_red_dark));
                }
                else
                {
                    fromLang.setText(R.string.prompt_detect_lang);
                    fromLang.setTextColor(getColor(android.R.color.darker_gray));
                    asyncTask = new TranslateAsyncTask(new CallbackAsyncTask()
                    {
                        @Override
                        public void onAsyncTaskDone(final String[] result)
                        {
                            int size = result.length;
                            if(size > 1)
                            {
                                fromLang.setText(R.string.prompt_pick_detected_languages);
                                fromLang.setTextColor(getColor(android.R.color.holo_blue_dark));
                                fromLang.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        String[] lang = Constants.getFormattedLang(result);

                                        Picker picker = new Picker();
                                        picker.list = lang;
                                        picker.callbackPickLang = callbackPickLang;
                                        picker.mode = PICK_LANG_FROM;
                                        picker.source = result;

                                        picker.show(getSupportFragmentManager(), "Picker1");
                                    }
                                });
                            }
                            else if( size == 1)
                            {
                                fromLang.setText(Constants.mapLanguages.get(result[0]));
                                fromLang.setTextColor(getColor(android.R.color.black));
                            }
                            else
                            {
                                fromLang.setText(R.string.prompt_language_no_detected);
                                fromLang.setTextColor(getColor(android.R.color.holo_red_dark));
                            }
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

        toLang.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String fromLanguage = fromLang.getText().toString();
                if(!fromLanguage.isEmpty() && !fromLanguage.equals(getString(R.string.prompt_language_no_detected)))
                {
                    asyncTask = new TranslateAsyncTask(new CallbackAsyncTask()
                    {
                        @Override
                        public void onAsyncTaskDone(final String[] result)
                        {

                            final String[] lang = Constants.getFormattedLang(result);
                            Picker picker = new Picker();
                            picker.list = lang;
                            picker.callbackPickLang = callbackPickLang;
                            picker.mode = PICK_LANG_TO;
                            picker.source = result;

                            picker.show(getSupportFragmentManager(), "Picker2");

                        }
                    }, Constants.MODE_TRANSLATE_SUPPORTED_LANG);

                    String code = Constants.getLanguageCode(fromLanguage);
                    if(code != null)
                    {
                        asyncTask.execute(code);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Language not stored..", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

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
                            textTranslated.setText(result[0]);
                        }

                    }, Constants.MODE_TRANSLATE_OPTIONS);

                    String srcLang = Constants.getLanguageCode(fromLang.getText().toString());
                    String tgtLang = Constants.getLanguageCode(toLang.getText().toString());
                    if (srcLang != null && tgtLang != null && !srcLang.equals(tgtLang))
                    {
                        asyncTask.execute(srcLang, tgtLang, textToTranslate.getText().toString());
                    }
                    else if(srcLang != null && tgtLang != null && srcLang.equals(tgtLang))
                    {
                        Toast.makeText(getApplicationContext(), "Both languages selected are the same..", Toast.LENGTH_LONG).show();
                    }
                    else if(tgtLang == null)
                    {
                        Toast.makeText(getApplicationContext(), "No language to translate set..", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        findViewById(R.id.imageView_swap).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String auxStr = fromLang.getText().toString();
                fromLang.setText(toLang.getText().toString());
                toLang.setText(auxStr);
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

    @Override
    public void onLangPicked(String result, int mode)
    {
        if(mode == PICK_LANG_FROM)
        {
            if(result != null)
            {
                fromLang.setTextColor(getColor(android.R.color.black));
            }
            else
            {
                fromLang.setText(R.string.prompt_language_no_detected);
                fromLang.setTextColor(getColor(android.R.color.holo_red_dark));
            }
        }
        else if(mode == PICK_LANG_TO)
        {
            if(result != null)
            {
                toLang.setText(result.split(" ")[0]);
                toLang.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
            }
            else
            {
                toLang.setText(R.string.prompt_language_no_detected);
                toLang.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
            }
        }
    }

    public static class Picker extends DialogFragment
    {

        private String[] list;
        private int selected;
        private CallbackPickLang callbackPickLang;
        private int mode;
        private String[] source;

        public Picker()
        {
            this.selected = -1;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            if(getActivity() != null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Pick language")
                        .setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener()
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
                                {
                                    callbackPickLang.onLangPicked(list[selected], mode);
                                }
                                else
                                {
                                    callbackPickLang.onLangPicked(null, mode);
                                }
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                callbackPickLang.onLangPicked(null, mode);
                            }
                        });

                return builder.create();
            }
            return super.onCreateDialog(savedInstanceState);
        }
    }
}
