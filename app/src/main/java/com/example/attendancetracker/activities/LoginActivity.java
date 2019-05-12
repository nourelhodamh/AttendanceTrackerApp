package com.example.attendancetracker.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancetracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.attendancetracker.Utils.displayToast;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mEmailWarning;
    private TextView mPasswordWarning;
    private String email;
    private String password;

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
                signUserIn();
            }
        });
    }

    private void initializeUI() {
        mEmail = findViewById(R.id.login_txt_email);
        mPassword = findViewById(R.id.loginActivity_txt_password);
        mLoginButton = findViewById(R.id.login_btn_login);
        mEmailWarning=findViewById(R.id.login_email_warning);
        mPasswordWarning=findViewById(R.id.login_password_warning);
    }


    private void signUserIn() {
        email = mEmail.getText().toString();
        password = mEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mEmailWarning.setVisibility(View.VISIBLE);
        } else if ((TextUtils.isEmpty(password))) {
            mPasswordWarning.setVisibility(View.VISIBLE);
        } else if (password.length() < 8) {
            mPasswordWarning.setVisibility(View.VISIBLE);
            mPasswordWarning.setText(getString(R.string.login_password_warning_two));
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                displayToast(LoginActivity.this, "Authentication failed.");
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
