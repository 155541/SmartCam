package revolhope.splanes.com.smartcam.view.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.controller.ContactTagAdapter;
import revolhope.splanes.com.smartcam.model.Tag;

public class PreContactManuallyTagsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precontact_tags);

        Tag[] tags = {
                new Tag(0, "Restaurant"),
                new Tag(0, "Bar"),
                new Tag(0, "Cheap"),
                new Tag(0, "Expensive"),
                new Tag(0, "Romantic"),
                new Tag(0, "Asia"),
                new Tag(0, "Sushi"),
                new Tag(0, "BBQ"),
                new Tag(0, "Hamburger"),
                new Tag(0, "Etc...")
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ContactTagAdapter adapter = new ContactTagAdapter(this, tags);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
