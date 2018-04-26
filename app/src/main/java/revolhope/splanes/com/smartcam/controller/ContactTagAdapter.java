package revolhope.splanes.com.smartcam.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.model.Tag;

public class ContactTagAdapter extends RecyclerView.Adapter<ContactTagAdapter.Holder> {

    private Tag[] tags;
    private Map<Tag, Holder> mapTagHolder;
    private LayoutInflater inflater;
    private Drawable markDrawable;

    public ContactTagAdapter(@NonNull Context context, Tag[] tags)
    {
        inflater = LayoutInflater.from(context);
        markDrawable = context.getDrawable(R.drawable.ic_done_black_24dp_small);
        if(tags == null)
        {
            this.tags = new Tag[1];
        }
        else
        {
            this.tags = tags;
        }
        mapTagHolder = new HashMap<>();
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

        if(tags != null && position != 0 && tags.length > position-1)
        {
            Tag tag = tags[position-1];
            holder.checkedTextView.setText(tag.getTagName());
            holder.checkedTextView.setCheckMarkDrawable(null);
            mapTagHolder.put(tag, holder);
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
        List<Tag> list = new ArrayList<>();
        for (Tag tag : tags)
        {
            if(mapTagHolder.containsKey(tag))
            {
                if(mapTagHolder.get(tag).checkedTextView.isChecked())
                {
                    list.add(tag);
                }
            }
        }
        return list;
    }

    public void setTags(List<Tag> tags)
    {
        this.tags = tags.toArray(new Tag[tags.size()]);
        notifyDatasetChanged();
    }

    class Holder extends RecyclerView.ViewHolder
    {

        private CheckedTextView checkedTextView;

        private Holder (View view)
        {
            super(view);

            checkedTextView = view.findViewById(R.id.checkedTextView);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(getAdapterPosition() == 0)
                    {
                        // TODO __implement: 'Add new category'
                    }
                    else
                    {
                        checkedTextView.toggle();
                        checkedTextView.setCheckMarkDrawable( checkedTextView.isChecked() ? markDrawable : null );
                    }
                }
            });
        }
    }
}
