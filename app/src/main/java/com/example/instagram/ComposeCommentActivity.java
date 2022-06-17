package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.data.model.Comment;
import com.example.instagram.data.model.Post;
import com.example.instagram.databinding.ActivityComposeCommentBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ComposeCommentActivity extends AppCompatActivity {
    public static final String TAG = "ComposeCommentActivity";
    Post post;
    Button btnSave;
    EditText etBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.instagram.databinding.ActivityComposeCommentBinding binding = ActivityComposeCommentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        btnSave = binding.btnSave;
        etBody = binding.etBody;

        post = getIntent().getParcelableExtra("post");

        btnSave.setOnClickListener(v -> {
            String body = etBody.getText().toString();
            Comment comment = new Comment();
            comment.setBody(body);
            comment.setUser(ParseUser.getCurrentUser());
            comment.setPost(post);

            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    finish();
                }
            });
        });

        Toast.makeText(this, post.getDescription(), Toast.LENGTH_SHORT).show();
    }
}