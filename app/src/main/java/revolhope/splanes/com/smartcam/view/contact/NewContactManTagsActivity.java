package revolhope.splanes.com.smartcam.view.contact;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.controller.ContactTagAdapter;
import revolhope.splanes.com.smartcam.database.AppRepository;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagSection;
import revolhope.splanes.com.smartcam.model.viewmodel.TagSectionViewModel;
import revolhope.splanes.com.smartcam.model.viewmodel.TagViewModel;

public class NewContactManTagsActivity extends AppCompatActivity
        implements ContactTagAdapter.AdapterCallback {

    private TagSection[] tagSectionArray;
    private Map<String, Tag[]> mTagMap;
    private ContactTagAdapter mAdapter;
    private AppRepository appRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact_tags);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle("Enter new Contact (2/2)");
            actionBar.setSubtitle("Set contact's tags");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        appRepository = new AppRepository(getApplication());

        TagSectionViewModel tagSectionViewModel = ViewModelProviders.of(this).get(TagSectionViewModel.class);
        TagViewModel tagViewModel = ViewModelProviders.of(this).get(TagViewModel.class);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ContactTagAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tagSectionViewModel.getAllTagSections().observe(this, new Observer<List<TagSection>>() {
            @Override
            public void onChanged(@Nullable final List<TagSection> tagSections)
            {

                if ( tagSections != null)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            int size = tagSections.size();
                            TagSection[] array = new TagSection[size];

                            for (int i = 0 ; i < size ; i++)
                            {
                                array[i] = tagSections.get(i);
                            }

                            tagSectionArray = array;
                            mAdapter.setTagSections(array);
                        }
                    });
                }
            }
        });

        tagViewModel.getAllTags().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(@Nullable List<Tag> tags) {

                if (tags != null)
                {
                    final Map<String, Tag[]> mMappedTags = new HashMap<>();
                    if (tagSectionArray != null)
                    {
                        String id;
                        for (TagSection section : tagSectionArray)
                        {
                            id = section.getTagSectionId();
                            List<Tag> tagList = new ArrayList<>();
                            for (Tag t : tags)
                            {
                                if (t.getTagSectionId().equals(id))
                                {
                                    tagList.add(t);
                                }
                            }
                            mMappedTags.put(id, tagList.toArray(new Tag[0]));
                        }
                    }
                    mTagMap = mMappedTags;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setMappedTags(mMappedTags);
                        }
                    });
                }

            }
        });
    }

// =================================================================================================
//                              ADAPTER_CALLBACK IMPLEMENTATION
// =================================================================================================

    @Override
    public void onSectionClick(final TagSection section, List<Integer> checkedTags)
    {
        List<String> list = new ArrayList<>();
        Tag[] tags = mTagMap.get(section.getTagSectionId());

        if ( tags != null)
        {
            for (Tag t : tags)
            {
                list.add(t.getTagName());
            }
        }

        ShowSectionTagsDialog dialog = new ShowSectionTagsDialog();

        dialog.tagNames = list.toArray(new String[0]);
        dialog.section = section;
        dialog.checkedList = checkedTags;
        dialog.callback = new ShowSectionTagsDialogCallback()
        {
            @Override
            public void onTagsSelected(final String sectionId, final List<Integer> checkedItems)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mAdapter.setCheckedTags(sectionId, checkedItems);
                    }
                });
            }

            @Override
            public void onNewTag() {

                CreateTagDialog createTagDialog = new CreateTagDialog();
                createTagDialog.section = section;
                createTagDialog.callback = new OnCreateTag()
                {
                    @Override
                    public void onCreate(String tagName, String sectionId)
                    {
                        if (tagName != null && !tagName.isEmpty() && sectionId != null)
                        {
                            Tag tag = new Tag(tagName, sectionId);
                            appRepository.insertTag(tag);

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "New tag saved.\nOpen section again to select it",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                        else
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Invalid tag name..",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    }
                };
                createTagDialog.show(getSupportFragmentManager(), "CreateTagDialog");
            }
        };
        dialog.show(getSupportFragmentManager(), "ShowSectionTagsDialog");
    }

    @Override
    public void onNewSection()
    {
        CreateSectionDialog createSectionDialog = new CreateSectionDialog();
        createSectionDialog.callback = new OnCreateSection()
        {
            @Override
            public void onCreate(String sectionName)
            {
                TagSection tagSection = new TagSection(sectionName);
                if (appRepository != null)
                {
                    appRepository.insertTagSection(tagSection);
                }
            }
        };
        createSectionDialog.show(getSupportFragmentManager(), "CreateSectionDialog");
    }



// =================================================================================================
//                                          DIALOGS
// =================================================================================================

    //////// SHOW TAGS
    public static class ShowSectionTagsDialog extends DialogFragment
    {
        private List<Integer> checkedList;
        private String[] tagNames;
        private TagSection section;
        private ShowSectionTagsDialogCallback callback;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {

            final Context context = getContext();
            if (context != null)
            {
                boolean[] checkedItems = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                if (checkedList != null && tagNames != null)
                {
                    int size = tagNames.length;
                    checkedItems = new boolean[size];

                    for ( int i = 0 ; i < size ; i++)
                    {
                        checkedItems[i] = false;
                    }
                    for (int index : checkedList)
                    {
                        checkedItems[index] = true;
                    }
                }

                builder.setTitle("Choose tag - " + section.getTagSectionName());
                builder.setMultiChoiceItems(tagNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b)
                    {
                        if (b)
                        {
                            if (!checkedList.contains(i))
                            {
                                checkedList.add(i);
                            }
                        }
                        else
                        {
                            if (checkedList.contains(i))
                            {
                                checkedList.remove(Integer.valueOf(i));
                            }
                        }
                    }
                });
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        callback.onTagsSelected(section.getTagSectionId(), checkedList);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                    }
                });
                builder.setNeutralButton("Add new", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        callback.onNewTag();
                    }
                });

                return builder.create();
            }
            else
            {
                return super.onCreateDialog(savedInstanceState);
            }
        }
    }

    //////// CREATE SECTION

    public static class CreateSectionDialog extends DialogFragment
    {
        private OnCreateSection callback;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Context context = getContext();
            Activity activity = getActivity();
            if (context != null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ViewGroup viewGroup = activity != null ? (ViewGroup) activity.findViewById(android.R.id.content) : null;
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_section, viewGroup , false);
                final EditText editText = view.findViewById(R.id.editText);

                builder.setTitle("New section");
                builder.setView(view);
                builder.setPositiveButton("create", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        callback.onCreate(editText.getText().toString());
                    }
                });
                builder.setNeutralButton("cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                return builder.create();
            }
            else
            {
                return super.onCreateDialog(savedInstanceState);
            }
        }
    }

    //////// CREATE TAG
    public static class CreateTagDialog extends DialogFragment
    {
        private TagSection section;
        private OnCreateTag callback;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Context context = getContext();
            Activity activity = getActivity();
            if (context != null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ViewGroup viewGroup = activity != null ? (ViewGroup) activity.findViewById(android.R.id.content) : null;
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_section, viewGroup , false);

                final EditText editText = view.findViewById(R.id.editText);

                builder.setTitle("New tag - " + section.getTagSectionName());
                builder.setView(view);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if (callback != null)
                        {
                            callback.onCreate(editText.getText().toString(), section.getTagSectionId());
                        }
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });

                return builder.create();
            }
            else
            {
                return super.onCreateDialog(savedInstanceState);
            }
        }
    }

// =================================================================================================
//                                  CALLBACK DIALOG: SHOW TAGS
// =================================================================================================

    private interface ShowSectionTagsDialogCallback
    {
        void onTagsSelected(String sectionId, List<Integer> checkedList);
        void onNewTag();
    }

// =================================================================================================
//                              CALLBACK DIALOG: CREATE SECTION
// =================================================================================================

    private interface OnCreateSection
    {
        void onCreate(String sectionName);
    }

// =================================================================================================
//                              CALLBACK DIALOG: CREATE TAG
// =================================================================================================

    private interface OnCreateTag
    {
        void onCreate(String tagName, String sectionId);
    }

}
