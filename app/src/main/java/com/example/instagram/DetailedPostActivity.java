package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.adapters.CommentsAdapter;
import com.example.instagram.data.model.Comment;
import com.example.instagram.data.model.Post;
import com.example.instagram.databinding.ActivityDetailedPostBinding;
import com.example.instagram.databinding.ActivityFeedBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

public class DetailedPostActivity extends AppCompatActivity {
    public static final String TAG = "DetailedPostActivity";
    private ActivityDetailedPostBinding binding;
    private RecyclerView rvComments;
    private CommentsAdapter adapter;
    private Post post;

    @Override
    protected void onRestart() {
        super.onRestart();

        refreshComments();
    }

    private void refreshComments() {
        adapter.mComments.clear();
        ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo(Comment.KEY_POST, post);
        query.orderByDescending("createdAt");
        query.include(Comment.KEY_AUTHOR);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                adapter.mComments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        TextView tvTimeStamp = binding.tvTimestamp;
        TextView tvDetailedDescription = binding.tvDetailedDescription;
        ImageView ivDetailedImage = binding.ivDetailedImage;
        TextView tvDetailedUsername = binding.tvDetailedUsername;
        ImageButton ibHeart = binding.ibHeart;
        ImageButton ibComment = binding.ibComment;

        rvComments = binding.rvComments;
        adapter = new CommentsAdapter();
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(adapter);

        Intent intent = getIntent();
        post = intent.getParcelableExtra("post");

        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        tvDetailedUsername.setText(post.getUser().getUsername());
        tvTimeStamp.setText(timeAgo);
        tvDetailedDescription.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(DetailedPostActivity.this).load(image.getUrl()).into(ivDetailedImage);
        }
        ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to compose comment activity
                Intent intent = new Intent(DetailedPostActivity.this, ComposeCommentActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });
        ibHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle liking posts
            }
        });
        ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo(Comment.KEY_POST, post);
        query.orderByDescending("createdAt");
        query.include(Comment.KEY_AUTHOR);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                adapter.mComments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}