package com.example.android.stackoverflow.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.stackoverflow.Model.QuestionItem;
import com.example.android.stackoverflow.R;

import java.util.List;

/**
 * Created by ashu on 07-04-2019.
 */

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder> {

    private List<QuestionItem> mItems;
    private Context mContext;
    private QuestionItemListener mItemListener;

    public QuestionRecyclerAdapter(Context context, List<QuestionItem> questions, QuestionItemListener itemListener) {
        mItems = questions;
        mContext = context;
        mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.question_list_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        QuestionItem item = mItems.get(i);
        TextView questionText = viewHolder.questionText;
        TextView tags = viewHolder.tagsText;
        TextView time = viewHolder.timeText;

        questionText.setText(item.getTitle());
        if (item.getTagsList() != null) {
            List<String> tagList = item.getTagsList();
            String tag = "";
            for (int x = 0; x < item.getTagsList().size(); x++) {
                tag = tag + tagList.get(x) + ", ";
            }
            tags.setText(tag);
        }
        if (((System.currentTimeMillis() / 1000L) - item.getCreationDate()) > 60) {
            time.setText(((System.currentTimeMillis() / 1000L) - item.getCreationDate()) / 60 + " hours ago");
        } else {
            time.setText(((System.currentTimeMillis() / 1000L) - item.getCreationDate()) / 6000 + " mins ago");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateQuestions(List<QuestionItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private QuestionItem getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface QuestionItemListener {
        void onQuestionClick(String link);

        void onLongClicked(QuestionItem position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView questionText, tagsText, timeText;
        QuestionItemListener mItemListener;

        public ViewHolder(@NonNull View itemView, QuestionItemListener questionItemListener) {

            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            tagsText = itemView.findViewById(R.id.tags_text);
            timeText = itemView.findViewById(R.id.time);
            this.mItemListener = questionItemListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            QuestionItem item = getItem(getAdapterPosition());
            this.mItemListener.onQuestionClick(item.getLink());
        }


        @Override
        public boolean onLongClick(View view) {
            this.mItemListener.onLongClicked(mItems.get(getAdapterPosition()));
            return true;
        }
    }
}
