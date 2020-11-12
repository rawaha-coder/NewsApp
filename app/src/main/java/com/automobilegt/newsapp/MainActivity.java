package com.automobilegt.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.automobilegt.newsapp.adapter.StoryAdapter;
import com.automobilegt.newsapp.databinding.ActivityMainBinding;
import com.automobilegt.newsapp.interfaces.ListItemClickListener;
import com.automobilegt.newsapp.model.Story;
import com.automobilegt.newsapp.utils.StoryLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListItemClickListener, LoaderManager.LoaderCallbacks<List<Story>> {

    private static final String GUARDIAN_BASE_URL = "https://content.guardianapis.com/search?";
    private static final int STORY_LOADER_ID = 1;
    private StoryAdapter mStoryAdapter;
    private List<Story> mStoryList;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mStoryList = new ArrayList<>();
        mStoryAdapter = new StoryAdapter(mStoryList, this);
        mBinding.rvStoryList.setAdapter(mStoryAdapter);
        mBinding.rvStoryList.setLayoutManager(new LinearLayoutManager(this));

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            mBinding.emptyView.setText(R.string.no_internet_connection);
            mBinding.loadingIndicator.setVisibility(View.GONE);
        } else {
            LoaderManager.getInstance(this).restartLoader(STORY_LOADER_ID, null, this);
        }
    }

    @Override
    public void onListItemClick(String webUrl) {
        Uri storyUri = Uri.parse(webUrl);
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, storyUri);
        startActivity(websiteIntent);
    }

    @NonNull
    @Override
    public Loader<List<Story>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri = Uri.parse(GUARDIAN_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("section", "technology");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("api-key", "test");
        return new StoryLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Story>> loader, List<Story> stories) {
        mStoryList.clear();
        mBinding.loadingIndicator.setVisibility(View.GONE);
        if (stories != null && !stories.isEmpty()) {
            mStoryList.addAll(stories);
            mStoryAdapter.notifyDataSetChanged();
        } else {
            mBinding.emptyView.setText(R.string.no_stories_found);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Story>> loader) {
        mBinding.emptyView.setText(R.string.no_stories_found);
    }
}