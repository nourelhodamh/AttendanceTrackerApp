package com.example.attendancetracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.attendancetracker.ClickHandler;
import com.example.attendancetracker.R;
import com.example.attendancetracker.databinding.ActivitySignUpBinding;
import com.example.attendancetracker.models.SignUpModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.attendancetracker.Utils.displayToast;

public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    private final String TAG = SignUpActivity.class.getSimpleName();


    ActivitySignUpBinding mBinding;
    SignUpModel signUpModel;
    ClickHandler clickHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
        signUpModel = new SignUpModel();
        mBinding.setSignUpModel(signUpModel);

        mBinding.setClickHandler(new ClickHandler(this) {
            @Override
            public void onButtonClick(View view) {
                super.onButtonClick(view);
                //progressBar.setVisibility(View.VISIBLE);
                createAccount();
                Log.v(TAG, "button clicked");
            }
        });
        mBinding.setClickHandler(new ClickHandler(this) {
            @Override
            public void onButtonClick(View view) {
                super.onButtonClick(view);
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
//        progressBar.setVisibility(View.GONE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && mAuth.getCurrentUser().isEmailVerified()) {
            updateUI(currentUser);
            Log.d(TAG, "ON RESUME");
        }

    }


    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(signUpModel.getEmail(), signUpModel.getPassword())
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                          progressBar.setVisibility(View.GONE);
                            signUpModel.setName("");
                            signUpModel.setEmail("");
                            signUpModel.setPassword("");
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




