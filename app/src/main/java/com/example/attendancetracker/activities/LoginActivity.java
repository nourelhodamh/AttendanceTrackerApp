package com.example.attendancetracker.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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

import com.example.attendancetracker.ClickHandler;
import com.example.attendancetracker.R;
import com.example.attendancetracker.databinding.ActivityLogInBinding;
import com.example.attendancetracker.models.LoginModel;
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
    ActivityLogInBinding mBinding;
    private LoginModel loginModel;
    private ClickHandler clickHandler;
    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        mBinding.setLoginModel(new LoginModel());

        clickHandler=new ClickHandler(this);

        mBinding.setClickHandler(new ClickHandler(this) {
            @Override
            public void onButtonClick(View view) {
                super.onButtonClick(view);
                signUserIn();
            }
        });

        mBinding.setClickHandler(new ClickHandler(this) {
            @Override
            public void onButtonClick(View view) {
                super.onButtonClick(view);
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
//        progressBar.setVisibility(View.GONE);

    }


    private void signUserIn() {
        mAuth.signInWithEmailAndPassword(loginModel.getEmail(), loginModel.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                                progressBar.setVisibility(View.GONE);
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
                        notifyUser("Model account has been disabled");
                    } else {
                        notifyUser(e.getLocalizedMessage());
                    }

                }

            }


        });
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
