package revolhope.splanes.com.smartcam.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.model.Tag;

public class ContactTagAdapter extends RecyclerView.Adapter<ContactTagAdapter.Holder> {

    private Tag[] tags;
    private List<Tag> checkedTags;
    private LayoutInflater inflater;

    public ContactTagAdapter(@NotNull Context context, Tag[] tags)
    {
        inflater = LayoutInflater.from(context);
        if(tags == null)
        {
            this.tags = new Tag[1];
        }
        else
        {
            this.tags = tags;
        }
        checkedTags = new ArrayList<>(this.tags.length);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0)
        {
            return new Holder(inflater.inflate(R.layout.holder_contact_tag_first_one, parent, false));
        }
        else
        {
            return new Holder(inflater.inflate(R.layout.holder_contact_tag, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        if(tags != null && position != 0 && tags.length > position)
        {
            Tag tag = tags[position];
            holder.checkedTextView.setText(tag.getTagName());
        }
    }

    @Override
    public int getItemCount() {
        if(tags != null)
        {
            return tags.length+1;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public List<Tag> getCheckedTags()
    {
        return checkedTags;
    }

    class Holder extends RecyclerView.ViewHolder
    {

        private CheckedTextView checkedTextView;

        private Holder (View view)
        {
            super(view);

            if(getAdapterPosition() == 0)
            {
                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // TODO __implement: 'Add new category'
                    }
                });
            }
            else
            {
                checkedTextView = view.findViewById(R.id.checkedTextView);
                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        int position = getAdapterPosition();

                        if(checkedTextView.isChecked())
                        {
                            checkedTags.remove(position);
                        }
                        else
                        {
                            if(tags.length > position)
                            {
                                checkedTags.add(position, tags[getAdapterPosition()]);
                            }
                        }
                        checkedTextView.setChecked(!checkedTextView.isChecked());
                    }
                });
            }
        }
    }
}
