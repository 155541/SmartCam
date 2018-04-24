package revolhope.splanes.com.smartcam.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.view.camera.PreviewCamActivity;
import revolhope.splanes.com.smartcam.view.contact.PickScanOrSetContactActivity;
import revolhope.splanes.com.smartcam.view.translate.TranslateActivity;

public class MasterActivity extends AppCompatActivity {

    private static final int PAGE_COUNT = 2;

    private boolean isShowingButtons;
    private FloatingActionButton fab;
    private FloatingActionButton fabContact;
    private FloatingActionButton fabTranslate;
    private FloatingActionButton fabNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        isShowingButtons = false;

        fab = findViewById(R.id.fab);
        fabContact = findViewById(R.id.fabContact);
        fabTranslate = findViewById(R.id.fabTranslate);
        fabNote = findViewById(R.id.fabNote);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(isShowingButtons)
                {
                    collapseFabs();
                    fab.setImageDrawable(getDrawable(R.drawable.ic_camera_enhance_white_24dp));
                }
                else
                {
                    expandFabs();
                    fab.setImageDrawable(getDrawable(R.drawable.ic_remove_white_24dp));
                }
                isShowingButtons = !isShowingButtons;
            }
        });

        View.OnClickListener listenerPreviewCam = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), PreviewCamActivity.class);
                startActivity(i);
                collapseFabs();
            }
        };
        fabNote.setOnClickListener(listenerPreviewCam);
        fabTranslate.setOnClickListener(listenerPreviewCam);

        fabContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Intent i = new Intent(getApplicationContext(), PreviewCamContactActivity.class);
                Intent i = new Intent(getApplicationContext(), PickScanOrSetContactActivity.class);
                startActivity(i);
                if (isShowingButtons)
                {
                    collapseFabs();
                    isShowingButtons = false;
                }
            }
        });

        findViewById(R.id.ic_translate).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), TranslateActivity.class);
                startActivity(i);
                if(isShowingButtons)
                {
                    collapseFabs();
                    isShowingButtons = false;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_master, menu);
        return true;
    }

    private void expandFabs()
    {
        ObjectAnimator translationXContact = ObjectAnimator.ofFloat(fabContact, "translationX", 0, -600);
        ObjectAnimator alphaContact = ObjectAnimator.ofFloat(fabContact, "alpha", 0, 1);

        ObjectAnimator translationXTranslate = ObjectAnimator.ofFloat(fabTranslate, "translationX", 0, -400);
        ObjectAnimator alphaTranslate = ObjectAnimator.ofFloat(fabTranslate, "alpha", 0, 1);

        ObjectAnimator translationXNote = ObjectAnimator.ofFloat(fabNote, "translationX", 0, -200);
        ObjectAnimator alphaNote = ObjectAnimator.ofFloat(fabNote, "alpha", 0, 1);

        translationXContact.setDuration(500);
        alphaContact.setDuration(500);

        translationXTranslate.setDuration(400);
        alphaTranslate.setDuration(400);

        translationXNote.setDuration(300);
        alphaNote.setDuration(300);


        AnimatorSet animatorSetContact = new AnimatorSet();
        animatorSetContact.playTogether(translationXContact, alphaContact);

        AnimatorSet animatorSetTranslate = new AnimatorSet();
        animatorSetContact.playTogether(translationXTranslate, alphaTranslate);

        AnimatorSet animatorSetNote = new AnimatorSet();
        animatorSetContact.playTogether(translationXNote, alphaNote);

        translationXContact.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fabContact.setVisibility(View.VISIBLE);
            }
        });

        translationXTranslate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fabTranslate.setVisibility(View.VISIBLE);
            }
        });

        translationXNote.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fabNote.setVisibility(View.VISIBLE);
            }
        });

        animatorSetContact.start();
        animatorSetTranslate.start();
        animatorSetNote.start();
    }

    private void collapseFabs()
    {
        ObjectAnimator translationXContact = ObjectAnimator.ofFloat(fabContact, "translationX", -600, 0);
        ObjectAnimator alphaContact = ObjectAnimator.ofFloat(fabContact, "alpha", 1, 0);

        ObjectAnimator translationXTranslate = ObjectAnimator.ofFloat(fabTranslate, "translationX", -400, 0);
        ObjectAnimator alphaTranslate = ObjectAnimator.ofFloat(fabTranslate, "alpha", 1, 0);

        ObjectAnimator translationXNote = ObjectAnimator.ofFloat(fabNote, "translationX", -200, 0);
        ObjectAnimator alphaNote = ObjectAnimator.ofFloat(fabNote, "alpha", 1, 0);

        translationXContact.setDuration(500);
        alphaContact.setDuration(300);

        translationXTranslate.setDuration(400);
        alphaTranslate.setDuration(200);

        translationXNote.setDuration(300);
        alphaNote.setDuration(100);


        AnimatorSet animatorSetContact = new AnimatorSet();
        animatorSetContact.playTogether(translationXContact, alphaContact);

        AnimatorSet animatorSetTranslate = new AnimatorSet();
        animatorSetContact.playTogether(translationXTranslate, alphaTranslate);

        AnimatorSet animatorSetNote = new AnimatorSet();
        animatorSetContact.playTogether(translationXNote, alphaNote);

        translationXContact.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fabContact.setVisibility(View.GONE);
            }
        });

        translationXTranslate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fabTranslate.setVisibility(View.GONE);
            }
        });

        translationXNote.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fabNote.setVisibility(View.GONE);
            }
        });

        animatorSetContact.start();
        animatorSetTranslate.start();
        animatorSetNote.start();

        fab.setImageDrawable(getDrawable(R.drawable.ic_camera_enhance_white_24dp));
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_master, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            if(getArguments() != null)
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount()
        {
            return PAGE_COUNT;
        }
    }
}
