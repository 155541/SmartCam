package revolhope.splanes.com.smartcam.view.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;

public class PreContactScanActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scan_result_contact);


        EditText editTextName = findViewById(R.id.editText_name);
        EditText editTextPhone = findViewById(R.id.editText_phone);
        EditText editTextMail = findViewById(R.id.editText_mail);
        EditText editTextLocation = findViewById(R.id.editText_address);
        EditText editTextWeb = findViewById(R.id.editText_web);
        EditText editTextDetected = findViewById(R.id.editText_detectedText);

        Intent i = getIntent();

        if(i != null && i.hasExtra(Constants.TEXTREAD))
        {
            String textRead = i.getStringExtra(Constants.TEXTREAD);

            editTextPhone.setText(tryExtractPhone(textRead));
            editTextMail.setText(tryExtractMail(textRead));
            editTextWeb.setText(tryExtractWeb(textRead));

            editTextDetected.setText(textRead);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String tryExtractPhone(@NotNull String text)
    {
        String[] texts = text.split(" ");

        for (String str : texts)
        {
            if(str.length() == 9)
            {
                try
                {
                    Integer.parseInt(str);
                    return str;
                }
                catch(NumberFormatException ignored)
                {
                }
            }
            else if (str.length() == 12)
            {
                String aux = str.substring(2,str.length()-1);
                try
                {
                    Integer.parseInt(aux);
                    return str;
                }
                catch(NumberFormatException ignored)
                {
                }
            }
        }
        return "";
    }

    // TODO: It must be improved
    private String tryExtractMail(@NotNull String text)
    {
        String[] texts = text.split(" ");

        for (String str : texts)
        {
            if(str.contains("@"))
            {
                return str;
            }
        }
        return "";
    }

    private String tryExtractWeb(@NotNull String text)
    {
        String[] texts = text.split(" ");

        for (String str : texts)
        {
            if(str.contains("://") ||
                    str.contains("https") ||
                    str.contains("http") ||
                    str.contains("www") ||
                    str.contains(".com") ||
                    str.contains(".es") ||
                    str.contains(".net") ||
                    str.contains(".org"))
            {
                return str;
            }
        }
        return "";
    }
}
