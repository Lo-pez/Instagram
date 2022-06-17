package com.example.instagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.instagram.R;
import com.example.instagram.adapters.UserPostAdapter;
import com.example.instagram.data.model.Post;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class ProfileFragment extends PostsFragment {

    public ParseUser user = ParseUser.getCurrentUser();
    protected UserPostAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSwipeContainer(view);

        rvPosts = view.findViewById(R.id.rvPosts);
        int columns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), columns, GridLayoutManager.VERTICAL, false);
        rvPosts.setLayoutManager(gridLayoutManager);
        allPosts = new ArrayList<>();
        adapter = new UserPostAdapter(getContext(), allPosts);
        rvPosts.setAdapter(adapter);


        queryPosts(null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void queryPosts(String maxId) {

        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
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
