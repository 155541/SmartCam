package revolhope.splanes.com.smartcam.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import revolhope.splanes.com.smartcam.R;
import revolhope.splanes.com.smartcam.model.Tag;
import revolhope.splanes.com.smartcam.model.TagSection;

public class ContactTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TagSection[] tagSections;
    private LayoutInflater inflater;
    private AdapterCallback callback;
    private Map<String, Tag[]> mMappedTags;
    private Map<String, List<Integer>> mapSectionTagsSelected;

    public ContactTagAdapter(@NonNull Context context, AdapterCallback callback)
    {
        this.inflater = LayoutInflater.from(context);
        this.callback = callback;
        this.mapSectionTagsSelected = new HashMap<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType == 0)
        {
            return new TagSectionHolder(inflater.inflate(R.layout.holder_contact_tag_section_first_one, parent, false));
        }
        else
        {
            return new TagSectionHolder(inflater.inflate(R.layout.holder_contact_tag_section, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        if (tagSections != null && tagSections.length > position-1 && position != 0)
        {
            TagSectionHolder sectionHolder = (TagSectionHolder) holder;
            TagSection tagSection = tagSections[position-1];

            List<Integer> listIndex = mapSectionTagsSelected.get(tagSection.getTagSectionId());
            Tag[] tags = mMappedTags.get(tagSection.getTagSectionId());
            StringBuilder sb = new StringBuilder();
            for (int index : listIndex)
            {
                sb.append(tags[index].getTagName()).append(" ");
            }
            String str = sb.toString();

            sectionHolder.name.setText(tagSection.getTagSectionName());
            if (!str.isEmpty())
            {
                sectionHolder.selectedTags.setText(str);
                sectionHolder.selectedTags.setVisibility(View.VISIBLE);
            }
            else
            {
                sectionHolder.selectedTags.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        if ( tagSections == null )
        {
            return 1;
        }
        else
        {
            return tagSections.length+1;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return position == 0 ? 0 : 1;
    }

    public void setTagSections(TagSection[] tagSections)
    {
        this.tagSections = tagSections;
        for (TagSection section : tagSections)
        {
            mapSectionTagsSelected.put(section.getTagSectionId(), new ArrayList<Integer>());
        }
        notifyDataSetChanged();
    }

    public void setMappedTags (Map<String, Tag[]> mMappedTags)
    {
        this.mMappedTags = mMappedTags;
    }

    public void setCheckedTags(String sectionId, List<Integer> checkedList)
    {
        mapSectionTagsSelected.put(sectionId, checkedList);
        notifyDataSetChanged();
    }

    // ADAPTER CALLBACK

    public interface AdapterCallback
    {
        void onSectionClick(TagSection section, List<Integer> checkedItems);
        void onNewSection();
    }

    // HOLDER

    private class TagSectionHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private TextView selectedTags;

        private TagSectionHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.textView_section);
            selectedTags = view.findViewById(R.id.textView_tags);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if ( getAdapterPosition() == 0 )
                    {
                        callback.onNewSection();
                    }
                    else
                    {
                        TagSection section = tagSections[getAdapterPosition()-1];
                        callback.onSectionClick(section, mapSectionTagsSelected.get(section.getTagSectionId()));
                    }
                }
            });
        }
    }
}
