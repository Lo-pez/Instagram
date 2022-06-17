package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.DetailedPostActivity;
import com.example.instagram.R;
import com.example.instagram.data.model.Post;
import com.parse.ParseFile;

import java.util.List;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder> {
    private final Context context;
    private final List<Post> userPosts;

    public UserPostAdapter(Context context, List<Post> userPosts) {
        this.context = context;
        this.userPosts = userPosts;
    }

    @NonNull
    @Override
    public UserPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPostAdapter.ViewHolder holder, int position) {
        Post post = userPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return userPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            ParseFile profileImage = post.getUser().getParseFile("profileImage");
            ivImage.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailedPostActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
            });
        }
    }
}
