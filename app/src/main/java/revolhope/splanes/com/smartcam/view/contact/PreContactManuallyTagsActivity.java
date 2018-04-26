package revolhope.splanes.com.smartcam.view.contact;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import java.util.List;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.controller.ContactTagAdapter;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagViewModel;

public class PreContactManuallyTagsActivity extends AppCompatActivity {

    private TagViewModel mTagViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_tags);

        mTagViewModel = ViewModelProviders.of(this).get(TagViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ContactTagAdapter adapter = new ContactTagAdapter(this, null);
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

        mTagViewModel.getAllTags().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(@Nullable final List<Tag> tags) {
                // Update the cached copy of the words in the adapter.
                adapter.setTags(tags);
            }
        });
    }
}
