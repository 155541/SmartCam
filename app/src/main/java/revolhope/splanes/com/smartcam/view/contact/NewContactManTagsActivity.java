package revolhope.splanes.com.smartcam.view.contact;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.controller.ContactTagAdapter;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagViewModel;

public class NewContactManTagsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_tags);

        TagViewModel mTagViewModel = ViewModelProviders.of(this).get(TagViewModel.class);

        LiveData<List<Tag>> liveTags = mTagViewModel.getAllTags();
        List<Tag> tags = new ArrayList<>();
        if(liveTags.getValue() != null)
        {
            tags = liveTags.getValue();
        }


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ContactTagAdapter adapter = new ContactTagAdapter(this, tags.toArray(new Tag[tags.size()]));
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

        liveTags.observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(@Nullable final List<Tag> tags) {
                // Update the cached copy of the words in the adapter.
                adapter.setTags(tags);
            }
        });
    }
}
