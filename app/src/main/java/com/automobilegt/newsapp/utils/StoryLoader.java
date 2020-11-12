package com.automobilegt.newsapp.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.automobilegt.newsapp.model.Story;

import java.util.List;

public class StoryLoader extends AsyncTaskLoader<List<Story>> {

    private String mUrl;
    public StoryLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Story> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return StoriesQuery.fetchStoryData(mUrl);
    }
}
