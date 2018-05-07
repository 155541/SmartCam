package revolhope.splanes.com.smartcam.view.contact;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.controller.ContactTagAdapter;
import revolhope.splanes.com.smartcam.database.AppRepository;
import revolhope.splanes.com.smartcam.model.Icon;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagSection;
import revolhope.splanes.com.smartcam.model.viewmodel.IconViewModel;
import revolhope.splanes.com.smartcam.model.viewmodel.TagSectionViewModel;
import revolhope.splanes.com.smartcam.model.viewmodel.TagViewModel;

public class NewContactManTagsActivity extends AppCompatActivity
        implements ContactTagAdapter.AdapterCallback {

    private TagSection[] tagSectionArray;
    private Map<String, Tag[]> mTagMap;
    private ContactTagAdapter mAdapter;
    private AppRepository appRepository;
    private List<Icon> mIcons;

    private Vibrator vibrator;

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
        IconViewModel iconViewModel = ViewModelProviders.of(this).get(IconViewModel.class);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ContactTagAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(mAdapter);

        GridLayoutManager  mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(position){
                    case 0:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);

        iconViewModel.getAllIcons().observe(this, new Observer<List<Icon>>() {
            @Override
            public void onChanged(@Nullable final List<Icon> icons)
            {

                if (icons != null)
                {
                    mIcons = icons;
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mAdapter.setIcons(icons);
                        }
                    });
                }
            }
        });

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

        findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
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

        Integer intResource = -300;
        for ( Icon ic : mIcons)
        {
            if ( ic.getIconId().equals(section.getTagSectionIconId()))
            {
                intResource = ic.getIconDrawableId();
            }
        }

        ShowSectionTagsDialog dialog = new ShowSectionTagsDialog();

        dialog.tagNames = list.toArray(new String[0]);
        dialog.section = section;
        dialog.checkedList = checkedTags;
        dialog.intResource = intResource;
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
        InsertSectionDialog sectionDialog = new InsertSectionDialog();
        sectionDialog.oldSection = null;
        sectionDialog.isInsert = true;
        sectionDialog.callback = new OnCreateSection()
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
            @Override
            public void onUpdate(TagSection tagSection) {
            }
        };
        sectionDialog.show(getSupportFragmentManager(), "CreateSectionDialog");
    }

    @Override
    public void onSectionLongClick(final TagSection section)
    {
        if ( vibrator == null )
        {
            vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            vibrator.vibrate(VibrationEffect.createOneShot(200, 60));
        }
        else if (vibrator != null)
        {
            vibrator.vibrate(200);
        }

        int intResource  = -300;
        for (Icon icon : mIcons)
        {
            if (icon.getIconId().equals(section.getTagSectionIconId()))
            {
                intResource = icon.getIconDrawableId();
            }
        }

        SectionTagOptionsDialog optionsDialog = new SectionTagOptionsDialog();
        optionsDialog.section = section;
        optionsDialog.intResource = intResource;
        optionsDialog.callback = new SectionTagOptionCallback()
        {
            @Override
            public void onOptionSelected(final int optionId)
            {
                if ( optionId != -1)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            switch (optionId)
                            {
                                case 0: // Show icons dialog UPDATE SECTION ICON

                                    ShowIconsDialog iconsDialog = new ShowIconsDialog();
                                    iconsDialog.mIcons = mIcons;
                                    iconsDialog.section = section;


                                    iconsDialog.show(getSupportFragmentManager(), "ShowIconsDialog");

                                    break;
                                case 1: // Show rename dialog UPDATE SECTION NAME

                                    InsertSectionDialog insertSectionDialog = new InsertSectionDialog();
                                    insertSectionDialog.oldSection = section;
                                    insertSectionDialog.isInsert = false;
                                    insertSectionDialog.callback = new OnCreateSection() {
                                        @Override
                                        public void onCreate(String sectionName) {}
                                        @Override
                                        public void onUpdate(TagSection tagSection)
                                        {
                                            if ( appRepository != null && tagSection != null)
                                            {
                                                System.out.println(" :......: To update :......: ");
                                                System.out.println(" :......: ID: " + tagSection.getTagSectionId());
                                                System.out.println(" :......: NAME: " + tagSection.getTagSectionName());
                                                System.out.println(" :......: ICON_ID: " + tagSection.getTagSectionIconId());
                                                appRepository.updateTagSection(tagSection);
                                            }
                                        }
                                    };
                                    insertSectionDialog.show(getSupportFragmentManager(), "InsertSectionDialog2");

                                    break;
                                case 2: // Show confirmation DELETE SECTION
                                    break;
                            }
                        }
                    });
                }
            }
        };
        optionsDialog.show(getSupportFragmentManager(), "SectionTagOptionsDialog");
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
        private int intResource;

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

                Spannable spannable = new SpannableString(section.getTagSectionName());
                spannable.setSpan(
                        new ForegroundColorSpan(
                                getResources().getColor(R.color.colorPrimaryDark, null)),
                        0, spannable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if ( intResource != -300)
                {
                    Drawable drawable = context.getDrawable(intResource);
                    if ( drawable != null )
                    {
                        drawable.setTint(context.getColor(R.color.colorPrimaryDark));
                        builder.setIcon(drawable);
                    }
                }
                builder.setTitle(spannable);
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

    //////// CREATE SECTION and UPDATE NAME

    public static class InsertSectionDialog extends DialogFragment
    {
        private OnCreateSection callback;
        private boolean isInsert;
        private TagSection oldSection;

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

                Spannable spannable = new SpannableString(isInsert ? "New section" : "Update section");
                spannable.setSpan(
                        new ForegroundColorSpan(
                                getResources().getColor(R.color.colorPrimaryDark, null)),
                        0, spannable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setTitle(spannable);
                builder.setView(view);
                builder.setPositiveButton(isInsert ? "create" : "update", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if ( isInsert )
                        {
                            callback.onCreate(editText.getText().toString());
                        }
                        else
                        {
                            oldSection.setTagSectionName(editText.getText().toString());
                            callback.onUpdate(oldSection);
                        }
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

                Spannable spannable = new SpannableString("New tag - " + section.getTagSectionName());
                spannable.setSpan(
                        new ForegroundColorSpan(
                                getResources().getColor(R.color.colorPrimaryDark, null)),
                        0, spannable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                builder.setTitle(spannable);
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

    //////// CREATE SECTION LONG CLICK OPTIONS
    public static class SectionTagOptionsDialog extends DialogFragment
    {
        private SectionTagOptionCallback callback;
        private TagSection section;
        private int intResource;
        private static final String[] settings = {
                "Change section icon",
                "Rename section",
                "Delete section"
        };
        private int index = -1;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Context context = getContext();
            if ( context != null )
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                Spannable spannable = new SpannableString(section.getTagSectionName());
                spannable.setSpan(
                        new ForegroundColorSpan(
                                getResources().getColor(R.color.colorPrimaryDark, null)),
                        0, spannable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if ( intResource != -300)
                {
                    Drawable drawable = context.getDrawable(intResource);
                    if ( drawable != null )
                    {
                        drawable.setTint(context.getColor(R.color.colorPrimaryDark));
                        builder.setIcon(drawable);
                    }
                }
                builder.setTitle(spannable);
                builder.setSingleChoiceItems(settings, -1, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        index = i;
                    }
                });

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if ( index == -1 ) index = 0;
                        callback.onOptionSelected(index);
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
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

    public static class ShowIconsDialog extends DialogFragment
    {
        private TagSection section;
        private List<Icon> mIcons;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Context context = getContext();
            Activity activity = getActivity();
            if ( context != null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ViewGroup viewGroup = activity != null ? (ViewGroup) activity.findViewById(android.R.id.content) : null;
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_pick_icon, viewGroup, false);

                RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

                IconAdapter iconAdapter = new IconAdapter(context, mIcons);
                recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
                recyclerView.setAdapter(iconAdapter);

                Spannable spannable = new SpannableString("Pick an icon for: " + section.getTagSectionName());
                spannable.setSpan(
                        new ForegroundColorSpan(
                                getResources().getColor(R.color.colorPrimaryDark, null)),
                        0, spannable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                builder.setTitle(spannable);
                builder.setView(view);
                builder.setPositiveButton("change", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });
                builder.setNegativeButton("cancel", null);


                return builder.create();
            }
            else
            {
                return super.onCreateDialog(savedInstanceState);
            }
        }

        private class IconAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        {
            private LayoutInflater inflater;
            private List<Icon> mIcons;

            private IconAdapter(Context context, List<Icon> mIcons)
            {
                inflater = LayoutInflater.from(context);
                this.mIcons = mIcons;
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                return new Holder(inflater.inflate(R.layout.holder_icon_picker, parent, false));
            }


            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
            {
                if ( mIcons != null && mIcons.size() > position )
                {
                    ((Holder) holder).icon.setImageResource(mIcons.get(position).getIconDrawableId());
                }
            }


            @Override
            public int getItemCount() {
                return 0;
            }

            private class Holder extends RecyclerView.ViewHolder
            {
                ImageView icon;

                private Holder (View view)
                {
                    super(view);
                    icon = view.findViewById(R.id.imageView_icon);
                    view.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {

                        }
                    });
                }
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
        void onUpdate(TagSection tagSection);
    }

// =================================================================================================
//                              CALLBACK DIALOG: CREATE TAG
// =================================================================================================

    private interface OnCreateTag
    {
        void onCreate(String tagName, String sectionId);
    }

// =================================================================================================
//                              CALLBACK DIALOG: SECTION TAG OPTIONS
// =================================================================================================

    private interface SectionTagOptionCallback
    {
        void onOptionSelected(int optionId);
    }

}
