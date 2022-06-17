package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.adapters.CommentsAdapter;
import com.example.instagram.data.model.Comment;
import com.example.instagram.data.model.Post;
import com.example.instagram.databinding.ActivityDetailedPostBinding;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class DetailedPostActivity extends AppCompatActivity {
    public static final String TAG = "DetailedPostActivity";
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
        query.findInBackground((objects, e) -> {
            if (e != null) {
                Log.e(TAG, e.getMessage());
                return;
            }
            adapter.mComments.addAll(objects);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.instagram.databinding.ActivityDetailedPostBinding binding = ActivityDetailedPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        TextView tvTimeStamp = binding.tvTimestamp;
        TextView tvDetailedDescription = binding.tvDetailedDescription;
        ImageView ivDetailedImage = binding.ivDetailedImage;
        TextView tvDetailedUsername = binding.tvDetailedUsername;
        ImageButton ibHeart = binding.ibHeart;
        ImageButton ibComment = binding.ibComment;
        TextView tvLikes = binding.tvLikes;

        RecyclerView rvComments = binding.rvComments;
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
        if (post.getLikedBy().contains(ParseUser.getCurrentUser())) {
            ibHeart.setBackgroundResource(R.drawable.ufi_heart_active);
        }
        else {
            ibHeart.setBackgroundResource(R.drawable.ufi_heart);
        }
        tvLikes.setText( post.getLikesCount() );
        if (image != null) {
            Glide.with(DetailedPostActivity.this).load(image.getUrl()).into(ivDetailedImage);
        }
        ibComment.setOnClickListener(v -> {
            // Go to compose comment activity
            Intent intent1 = new Intent(DetailedPostActivity.this, ComposeCommentActivity.class);
            intent1.putExtra("post", post);
            startActivity(intent1);
        });
        ibHeart.setOnClickListener(v -> {
            List<ParseUser> likedBy = post.getLikedBy();
            if (likedBy.contains(ParseUser.getCurrentUser())) {
                likedBy.remove(ParseUser.getCurrentUser());
                ibHeart.setBackgroundResource(R.drawable.ufi_heart);
            }
            else {
                ibHeart.setBackgroundResource(R.drawable.ufi_heart_active);
                likedBy.add(ParseUser.getCurrentUser());
            }
            tvLikes.setText( String.valueOf( post.getLikedBy().size()) );
            post.setLikedBy(likedBy);
            post.saveInBackground(); // uploads new value back to parse
        });
        ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo(Comment.KEY_POST, post);
        query.orderByDescending("createdAt");
        query.include(Comment.KEY_AUTHOR);
        query.findInBackground((objects, e) -> {
            if (e != null) {
                Log.e(TAG, e.getMessage());
                return;
            }
            adapter.mComments.addAll(objects);
            adapter.notifyDataSetChanged();
        });
    }
}