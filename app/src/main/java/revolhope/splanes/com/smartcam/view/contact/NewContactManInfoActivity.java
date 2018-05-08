package revolhope.splanes.com.smartcam.view.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;

public class NewContactManInfoActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout_Name;
    private TextInputLayout textInputLayout_Phone;
    private TextInputLayout textInputLayout_Mail;
    private TextInputLayout textInputLayout_Address;
    private TextInputLayout textInputLayout_Web;

    private TextInputEditText textInputEditText_Name;
    private TextInputEditText textInputEditText_Phone;
    private TextInputEditText textInputEditText_Mail;
    private TextInputEditText textInputEditText_Address;
    private TextInputEditText textInputEditText_Web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_fields);


        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null)
        {
            actionBar.setTitle("Enter new Contact (1/2)");
            actionBar.setSubtitle("Set information to store");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textInputLayout_Name = findViewById(R.id.textInput_contact);
        textInputLayout_Phone = findViewById(R.id.textInput_phone);
        textInputLayout_Mail = findViewById(R.id.textInput_mail);
        textInputLayout_Address = findViewById(R.id.textInput_address);
        textInputLayout_Web = findViewById(R.id.textInput_web);

        textInputEditText_Name = findViewById(R.id.textInputEditText_name);
        textInputEditText_Phone = findViewById(R.id.textInputEditText_phone);
        textInputEditText_Mail = findViewById(R.id.textInputEditText_mail);
        textInputEditText_Address = findViewById(R.id.textInputEditText_address);
        textInputEditText_Web = findViewById(R.id.textInputEditText_web);

        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(checkFields())
                {
                    Intent i = new Intent(getApplicationContext(), NewContactManTagsActivity.class);
                    i.putExtra(Constants.CONTACT_NAME, textInputEditText_Name.getText().toString());
                    i.putExtra(Constants.CONTACT_PHONE, textInputEditText_Phone.getText().toString());
                    i.putExtra(Constants.CONTACT_MAIL, textInputEditText_Mail.getText().toString());
                    i.putExtra(Constants.CONTACT_LOCATION, textInputEditText_Address.getText().toString());
                    i.putExtra(Constants.CONTACT_WEB, textInputEditText_Web.getText().toString());
                    NewContactManTagsActivity.finishCallback = new ContactFinishedCallback() {
                        @Override
                        public void onContactSet() {
                            finish();
                        }
                    };
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Another field is mandatory", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkFields()
    {
        String str;
        boolean bName = true;
        boolean bPhone = true;
        boolean bMail = true;
        boolean bAddress = true;
        boolean bWeb = true;

        textInputLayout_Name.setErrorEnabled(false);
        textInputLayout_Phone.setErrorEnabled(false);
        textInputLayout_Mail.setErrorEnabled(false);
        textInputLayout_Address.setErrorEnabled(false);
        textInputLayout_Web.setErrorEnabled(false);

        str = textInputEditText_Name.getText().toString();
        if (str.isEmpty())
        {
            bName = false;
            textInputLayout_Name.setError("Contact name is mandatory");
            textInputLayout_Name.setErrorEnabled(true);
            textInputLayout_Name.requestFocus();
        }
        else if (str.length() < 4)
        {
            bName = false;
            textInputLayout_Name.setError("Contact name must be longer than 4 chars");
            textInputLayout_Name.setErrorEnabled(true);
            textInputLayout_Name.requestFocus();
        }

        str = textInputEditText_Phone.getText().toString();
        if (!str.isEmpty())
        {
            if (str.length() < 9)
            {
                bPhone = false;
                textInputLayout_Phone.setError("Minimum 9 numbers are mandatory");
                textInputLayout_Phone.setErrorEnabled(true);
                textInputLayout_Phone.requestFocus();
            }
        }
        else
        {
            bPhone = false;
        }

        str = textInputEditText_Mail.getText().toString();
        if (!str.isEmpty())
        {
            if (!str.contains("@") || (!str.contains(".com") || !str.contains(".es") || !str.contains(".cat")))
            {
                bMail = false;
                textInputLayout_Mail.setError("Invalid email format");
                textInputLayout_Mail.setErrorEnabled(true);
                textInputLayout_Mail.requestFocus();
            }
        }
        else
        {
            bMail = false;
        }

        str = textInputEditText_Address.getText().toString();
        if (!str.isEmpty())
        {
            if (str.length() < 4)
            {
                bAddress = false;
                textInputLayout_Address.setError("Address must be longer than 4 chars");
                textInputLayout_Address.setErrorEnabled(true);
                textInputLayout_Address.requestFocus();
            }
        }
        else
        {
            bAddress = false;
        }

        str = textInputEditText_Web.getText().toString();
        if (!str.isEmpty())
        {
            if (!str.contains("www.") && (!str.contains(".es") || !str.contains(".com") || !str.contains(".cat")))
            {
                bWeb = false;
                textInputLayout_Web.setError("Invalid website format");
                textInputLayout_Web.setErrorEnabled(true);
                textInputLayout_Web.requestFocus();
            }
        }
        else
        {
            bWeb = false;
        }


        return bName && (bPhone || bMail || bAddress || bWeb);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    interface ContactFinishedCallback extends Serializable
    {
        void onContactSet();
    }
}
