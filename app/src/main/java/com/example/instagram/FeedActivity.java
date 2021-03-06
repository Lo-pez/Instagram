package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.data.model.Post;
import com.example.instagram.databinding.ActivityFeedBinding;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    public static final String TAG = "FeedActivity";
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private ActivityFeedBinding binding;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setUpSwipeContainer();

        rvPosts = binding.rvPosts;
        linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(linearLayoutManager);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);
        rvPosts.setAdapter(adapter);

        queryPosts(null);

        setUpEndlessScrolling();

        // initialize the array that will hold posts and create a PostsAdapter
    }

    private void setUpEndlessScrolling() {
        // Retain an instance so that you can call `resetState()` for fresh searches
        // Triggered only when new data needs to be appended to the list
        // Add whatever code is needed to append new items to the bottom of the list
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);
    }

    public void loadNextDataFromApi() {
        queryPosts(allPosts.get(allPosts.size() - 1).getObjectId());
    }

    private void setUpSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) binding.swipeContainer;
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            adapter.clear();
            queryPosts(null);
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void queryPosts(String maxId) {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground((posts, e) -> {
            // check for errors
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                return;
            }

            // for debugging purposes let's print every post description to logcat
            for (Post post : posts) {
                Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
            }

            // save received posts to list and notify adapter of new data
            allPosts.addAll(posts);
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        });
    }
}
