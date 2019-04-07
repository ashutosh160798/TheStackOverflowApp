package com.example.android.stackoverflow.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.stackoverflow.Model.TagItem;

import java.util.List;

/**
 * Created by ashu on 06-04-2019.
 */

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.ViewHolder> {

    private List<TagItem> mItems;
    private Context mContext;
    private TagItemListener mItemListener;

    public TagRecyclerAdapter(Context context, List<TagItem> tags, TagItemListener itemListener) {
        mItems = tags;
        mContext = context;
        mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TagItem item = mItems.get(i);
        TextView textView = viewHolder.tagText;
        textView.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateTags(List<TagItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private TagItem getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface TagItemListener {
        void onTagClick(String name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tagText;
        TagItemListener mItemListener;

        public ViewHolder(@NonNull View itemView, TagItemListener tagItemListener) {

            super(itemView);
            tagText = itemView.findViewById(android.R.id.text1);
            this.mItemListener = tagItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TagItem item = getItem(getAdapterPosition());
            this.mItemListener.onTagClick(item.getName());
        }


    }
}
