package com.example.attendancetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.attendancetracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import static com.example.attendancetracker.Utils.displayToast;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mSignUpButton;
    private TextView mEmailWarning;
    private TextView mPasswordWarning;
    private ProgressBar progressBar;


    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initializeUI();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signUserIn();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

    }

    private void initializeUI() {
        mEmail = findViewById(R.id.login_txt_email);
        mPassword = findViewById(R.id.loginActivity_txt_password);
        mLoginButton = findViewById(R.id.login_btn_login);
        mSignUpButton = findViewById(R.id.txt_signUp);
        mEmailWarning = findViewById(R.id.login_email_warning);
        mPasswordWarning = findViewById(R.id.login_password_warning);
        progressBar = findViewById(R.id.login_progressBar);

    }


    private void signUserIn() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEmailWarning.setVisibility(View.VISIBLE);
        } else if ((TextUtils.isEmpty(password))) {
            mPasswordWarning.setVisibility(View.VISIBLE);
        } else if (password.length() < 8) {
            mPasswordWarning.setVisibility(View.VISIBLE);
            mPasswordWarning.setText(getString(R.string.password_length_warning));
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }

                            // ...
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    updateUI(null);

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        notifyUser("Invalid password");
                    } else if (e instanceof FirebaseAuthInvalidUserException) {
                        String errorCode =
                                ((FirebaseAuthInvalidUserException) e).getErrorCode();

                        if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                            notifyUser("No matching account found");
                        } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                            notifyUser("User account has been disabled");
                        } else {
                            notifyUser(e.getLocalizedMessage());
                        }

                    }

                }


            });
        }


    }


    private void notifyUser(String localizedMessage) {
        displayToast(getApplicationContext(), localizedMessage);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
