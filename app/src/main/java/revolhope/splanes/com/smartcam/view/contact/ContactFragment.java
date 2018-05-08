package revolhope.splanes.com.smartcam.view.contact;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.helper.Constants;
import revolhope.splanes.com.smartcam.model.Contact;
import revolhope.splanes.com.smartcam.model.Icon;
import revolhope.splanes.com.smartcam.model.viewmodel.ContactViewModel;
import revolhope.splanes.com.smartcam.model.viewmodel.IconViewModel;

public class ContactFragment extends Fragment
{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Context context = getContext();
        final Activity activity = getActivity();
        if (context != null && activity != null) {
            View view = inflater.inflate(R.layout.fragment_contacts, container, false);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView_contacts);
            final ContactAdapter contactAdapter = new ContactAdapter(context);

            ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
            contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>()
            {
                @Override
                public void onChanged(final @Nullable List<Contact> contacts)
                {
                    if (contacts != null)
                    {
                        activity.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                contactAdapter.setContacts(contacts.toArray(new Contact[0]));
                                contactAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            });

            return view;
        }
        else
        {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder>
    {
        private LayoutInflater mInflater;
        private Contact[] mContacts;

        private ContactAdapter(Context context)
        {
            this.mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ContactAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(mInflater.inflate(R.layout.holder_contact_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContactAdapter.Holder holder, int position)
        {

        }

        @Override
        public int getItemCount() {
            if ( mContacts != null )
            {
                return mContacts.length;
            }
            else
            {
                return 0;
            }
        }

        private void setContacts(Contact[] contacts)
        {
            this.mContacts = contacts;
        }

        class Holder extends RecyclerView.ViewHolder
        {
            Holder(View view)
            {
                super(view);
            }
        }
    }







        /*

        Bundle bundle = getArguments();


        if ( null != context && null != bundle && null != activity )
        {
            if (contact != null)
            {
                TextView textView_Name = view.findViewById(R.id.textView_name);
                TextView textViewPhone = view.findViewById(R.id.textView_phone);
                RecyclerView recyclerView = view.findViewById(R.id.recyclerView_tags);

                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false));
                final IconAdapter iconAdapter = new IconAdapter(context);

                IconViewModel iconViewModel = ViewModelProviders.of(this).get(IconViewModel.class);
                iconViewModel.getAllIcons().observe(this, new Observer<List<Icon>>()
                {
                    @Override
                    public void onChanged(final @Nullable List<Icon> icons)
                    {
                        activity.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                iconAdapter.setIcons(icons);
                                iconAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

                textView_Name.setText(contact.getContactName());
                boolean hasPhone = true;
                if (contact.getContactPhone().isEmpty())
                {
                    textViewPhone.setText(R.string.hint_no_phone);
                    hasPhone = false;
                }
                else
                {
                    textViewPhone.setText(contact.getContactPhone());
                }

                if (hasPhone)
                {
                    view.findViewById(R.id.linearLayout_phone).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            // TODO: Make call?
                        }
                    });
                }
                else
                {
                    view.findViewById(R.id.linearLayout_phone)
                            .setBackgroundColor(getResources().getColor(android.R.color.darker_gray, null));
                    view.findViewById(R.id.imageView_icon)
                            .setForegroundTintList(
                                    ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark,
                                                    null)));
                }

            }

            return view;
        }
        else
        {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    private class IconAdapter extends RecyclerView.Adapter<IconAdapter.IcHolder>
    {
        private LayoutInflater mInflater;
        private List<Icon> mIcons;
        private Contact[] contacts;

        private IconAdapter(Context context)
        {
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public IcHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new IcHolder(mInflater.inflate(R.layout.holder_icon_picker, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull IcHolder holder, int position) {

            if (mIcons != null && mIcons.size() > position)
            {
                Icon ic = mIcons.get(position);
                if ( ic != null)
                {
                    holder.imageViewIcon.setImageResource(ic.getIconDrawableId());
                    holder.imageViewIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark, null)));
                }
            }

        }

        @Override
        public int getItemCount() {
            if (mIcons != null)
            {
                return mIcons.size();
            }
            else
            {
                return 0;
            }
        }

        private void setIcons(List<Icon> mIcons)
        {
            this.mIcons = mIcons;
        }

        class IcHolder extends RecyclerView.ViewHolder
        {
            ImageView imageViewIcon;
            private IcHolder(View v)
            {
                super(v);
                imageViewIcon = v.findViewById(R.id.imageView_icon);
            }
        }
    }

    */
}
