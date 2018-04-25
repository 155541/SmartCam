package revolhope.splanes.com.smartcam.view.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.controller.ContactTagAdapter;
import revolhope.splanes.com.smartcam.model.Tag;

public class PreContactManuallyTagsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_tags);

        Tag[] tags = {
                new Tag("Restaurant"),
                new Tag("Bar"),
                new Tag("Cheap"),
                new Tag("Expensive"),
                new Tag("Romantic"),
                new Tag("Asia"),
                new Tag("Sushi"),
                new Tag("BBQ"),
                new Tag("Hamburger"),
                new Tag("Etc...")
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ContactTagAdapter adapter = new ContactTagAdapter(this, tags);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                for (Tag t : adapter.getCheckedTags())
                {
                    System.out.println(" :......: TAG Id :......: " + t.getTagId());
                    System.out.println(" :......: TAG Name :......: " + t.getTagName());
                }
            }
        });
    }
}
