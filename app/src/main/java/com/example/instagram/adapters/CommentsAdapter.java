package com.example.instagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.data.model.Comment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    public ArrayList<Comment> mComments = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvBody;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        }
    }


    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View commentView = inflater.inflate(R.layout.item_comment, parent, false);

        ViewHolder viewHolder = new ViewHolder(commentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Comment comment = mComments.get(position);

        holder.tvBody.setText(comment.getBody());
        holder.tvAuthor.setText(comment.getUser().getUsername());

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
}
