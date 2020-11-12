package com.automobilegt.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automobilegt.newsapp.R;
import com.automobilegt.newsapp.interfaces.ListItemClickListener;
import com.automobilegt.newsapp.model.Story;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    ListItemClickListener mListItemClickListener;
    private List<Story> mStoryList;

    public StoryAdapter(List<Story> mStoryList, ListItemClickListener listItemClickListener) {
        this.mStoryList = mStoryList;
        this.mListItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = mStoryList.get(position);
        holder.storyTitle.setText(story.getTitle());
        holder.storySection.setText(story.getSection());
        holder.storyDate.setText(story.getPublicationDate());
        holder.storyAuthor.setText(story.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView storyTitle;
        TextView storySection;
        TextView storyDate;
        TextView storyAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyTitle = itemView.findViewById(R.id.tv_story_title);
            storySection = itemView.findViewById(R.id.tv_story_section);
            storyDate = itemView.findViewById(R.id.tv_story_date);
            storyAuthor = itemView.findViewById(R.id.tv_story_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListItemClickListener.onListItemClick(mStoryList.get(getAdapterPosition()).getWebUrl());
        }
    }
}