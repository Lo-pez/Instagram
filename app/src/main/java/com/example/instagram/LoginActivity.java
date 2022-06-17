package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.parse.ParseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    public static final int RC_SIGN_IN = 7;
    GoogleSignInClient mGoogleSignInClient;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        setUpGoogleSignIn();

        EditText etUsername = binding.etUsername;
        EditText etPassword = binding.etPassword;
        Button btnLogin = binding.btnLogin;
        Button btnSignUp = binding.btnSignUp;
        SignInButton btnGoogleSignIn = binding.signInButton;
        btnLogin.setOnClickListener(v -> {
            Log.i(TAG, "onClick Login Button");
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(username, password);
        });
        Objects.requireNonNull(btnSignUp).setOnClickListener(v -> {
            Log.i(TAG, "onClick SignUp Button");
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            signUpUser(username, password);
        });
        Objects.requireNonNull(btnGoogleSignIn).setOnClickListener(v -> {
            if (v.getId() == R.id.sign_in_button) {
                signIn();
            }
        });
    }

    private void setUpGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            goMainActivity();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "Google sign in works");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "OnActivityResult started");
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            Log.d(TAG, "OnActivityResult returned");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            loginUser(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void loginUser(GoogleSignInAccount account) {
        ParseUser.logInInBackground(Objects.requireNonNull(account.getDisplayName()), null, (user, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with login", e);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            goMainActivity();
            Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_LONG).show();
        });
    }


    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to log in user " + username);
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with login", e);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            goMainActivity();
            Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_LONG).show();
        });
    }


    private void signUpUser(String username, String password) {
        Log.i(TAG, "Attempting to sign up " + username);
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Error while saving new post!", e);
                Toast.makeText(LoginActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "User was successfully signed up!");
            loginUser(username, password);
        });
        ParseUser.logInInBackground(username, password, (user1, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with login", e);
                return;
            }
            goMainActivity();
            Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_LONG).show();
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}