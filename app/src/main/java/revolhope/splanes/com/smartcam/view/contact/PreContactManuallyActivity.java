package revolhope.splanes.com.smartcam.view.contact;

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

public class PreContactManuallyActivity extends AppCompatActivity {

    private EditText editText_Name;
    private EditText editText_Phone;
    private EditText editText_Mail;
    private EditText editText_Address;
    private EditText editText_Web;

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

        editText_Name = findViewById(R.id.editText_name);
        editText_Phone = findViewById(R.id.editText_phone);
        editText_Mail = findViewById(R.id.editText_mail);
        editText_Address = findViewById(R.id.editText_address);
        editText_Web = findViewById(R.id.editText_web);

        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(checkFields())
                {
                    Intent i = new Intent(getApplicationContext(), PreContactManuallyTagsActivity.class);
                    i.putExtra(Constants.CONTACT_NAME, editText_Name.getText().toString());
                    i.putExtra(Constants.CONTACT_PHONE, editText_Phone.getText().toString());
                    i.putExtra(Constants.CONTACT_MAIL, editText_Mail.getText().toString());
                    i.putExtra(Constants.CONTACT_LOCATION, editText_Address.getText().toString());
                    i.putExtra(Constants.CONTACT_WEB, editText_Web.getText().toString());
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "'Name' and one more fields are mandatory", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkFields()
    {
        return !editText_Name.getText().toString().isEmpty() &&
                (!editText_Phone.getText().toString().isEmpty() ||
                !editText_Mail.getText().toString().isEmpty() ||
                !editText_Address.getText().toString().isEmpty() ||
                !editText_Web.getText().toString().isEmpty());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
