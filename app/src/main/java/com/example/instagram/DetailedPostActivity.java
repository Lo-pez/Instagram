package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.data.model.Post;
import com.example.instagram.databinding.ActivityDetailedPostBinding;
import com.example.instagram.databinding.ActivityFeedBinding;
import com.parse.ParseFile;

import java.util.Date;

public class DetailedPostActivity extends AppCompatActivity {
    private ActivityDetailedPostBinding binding;
    private TextView tvTimeStamp;
    private TextView tvDetailedDescription;
    private ImageView ivDetailedImage;
    private TextView tvDetailedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tvTimeStamp = binding.tvTimestamp;
        tvDetailedDescription = binding.tvDetailedDescription;
        ivDetailedImage = binding.ivDetailedImage;
        tvDetailedUsername = binding.tvDetailedUsername;


        Intent intent = getIntent();
        Post post = intent.getParcelableExtra("post");

        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        tvDetailedUsername.setText(post.getUser().getUsername());
        tvTimeStamp.setText(timeAgo);
        tvDetailedDescription.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(DetailedPostActivity.this).load(image.getUrl()).into(ivDetailedImage);
        }
    }
}