package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.DetailedPostActivity;
import com.example.instagram.MainActivity;
import com.example.instagram.R;
import com.example.instagram.data.model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private final Context context;
    private final List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvUsername;
        private final ImageView ivImage;
        private final TextView tvDescription;
        private final ImageView ivProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ConstraintLayout clPost = itemView.findViewById(R.id.clPost);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ImageButton ibHeart = itemView.findViewById(R.id.ibHeart);
            ImageButton ibComment = itemView.findViewById(R.id.ibComment);
            ImageButton ibSave = itemView.findViewById(R.id.ibSave);
            ImageButton ibShare = itemView.findViewById(R.id.ibShare);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            ParseFile profileImage = post.getUser().getParseFile("profileImage");
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).circleCrop().into(ivProfileImage);
            }
            ivImage.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailedPostActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
            });
            ivProfileImage.setOnClickListener(v -> {
                // tell the activity to go to the Profile Fragment
                ((MainActivity)context).goToProfileFragment(post.getUser());
            });
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    private void logOut() {
        ParseUser.logOut();
        ((MainActivity)context).finish();
        if (ParseUser.getCurrentUser() == null) {
            Log.i("profileFragment", "User successfully logged out!");
        }
        else {
            Log.e("profileFragment", "User not logged out!");
        }
    }
}