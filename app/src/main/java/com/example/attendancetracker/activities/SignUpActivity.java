package com.example.attendancetracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.attendancetracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.attendancetracker.Utils.displayToast;

public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignIn;
    private TextView mName;
    private TextView mLogin;
    private TextView mEmailWarning;
    private TextView mPasswordWarning;
    private ProgressBar progressBar;
    private final String TAG = SignUpActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeUI();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                createAccount();

            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onStart() {
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && mAuth.getCurrentUser().isEmailVerified()) {
            updateUI(currentUser);
            Log.d(TAG, "ON RESUME");
        }

    }

    private void initializeUI() {
        mName = findViewById(R.id.txt_name_signup);
        mEmail = findViewById(R.id.txt_email);
        mPassword = findViewById(R.id.txt_password);
        mSignIn = findViewById(R.id.btn_signUp);
        mLogin = findViewById(R.id.txt_login);
        progressBar = findViewById(R.id.progress_Bar);
        mEmailWarning = findViewById(R.id.txt_email_warning);
        mPasswordWarning = findViewById(R.id.txt_password_warning);
    }


    private void createAccount() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEmailWarning.setVisibility(View.VISIBLE);
        }
        if ((TextUtils.isEmpty(password))) {
            mPasswordWarning.setVisibility(View.VISIBLE);
        }
        if (password.length() < 8) {
            mPasswordWarning.setVisibility(View.VISIBLE);
            mPasswordWarning.setText(getString(R.string.password_length_warning));
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);

                            mName.setText("");
                            mEmail.setText("");
                            mPassword.setText("");
                            verifyEmail();


                        } else {
                            // If sign in fails, display a message to the user.
                            displayToast(SignUpActivity.this, "Authentication failed.");
                            updateUI(null);


                        }

                        // ...
                    }
                });

    }

    private void verifyEmail() {

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");

                                if (user.isEmailVerified()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    displayToast(getApplicationContext(), "Please Check your Email for verification");
                                }


                            } else {
                                Log.d(TAG, "Email not sent.");
                            }
                        }
                    });
        }


    }

    private void updateUI(FirebaseUser user) {

        if (user != null && user.isEmailVerified()) {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}




