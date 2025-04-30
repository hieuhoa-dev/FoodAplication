package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.Components.CustomDialog;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setVariable();
        checkSignupSuccess();

    }

    private void checkSignupSuccess() {
        boolean signupSuccess = getIntent().getBooleanExtra("signup_success", false);
        if (signupSuccess) {
            CustomDialog dialog = new CustomDialog(this);
            dialog.DialogNormal("Success", "Sign up successfully.");
        }
    }


    private void setVariable() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.userEdit.getText().toString();
                String password = binding.passEdit.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    new CustomDialog(LoginActivity.this)
                            .DialogNormal("Error", "Please fill in all fields");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish(); // không cho back về login
                            } else {
                                Exception e = task.getException();
                                CustomDialog dialog = new CustomDialog(LoginActivity.this);
                                if (e instanceof FirebaseAuthInvalidUserException) {
                                    dialog.DialogNormal("Sign in failed", "Account does not exist.");
                                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    dialog.DialogNormal("Sign in failed", "Invalid email or password.");
                                } else if (e instanceof FirebaseNetworkException) {
                                    dialog.DialogNormal("Network Error", "Please check your internet connection.");
                                } else {
                                    dialog.DialogNormal("Sign in failed", "An unexpected error occurred. Please try again.");
                                }
                            }
                        });
            }
        });

        binding.signupTxt.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        binding.imgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.passEdit.getInputType() == 129) {
                    binding.passEdit.setInputType(128);
                    binding.imgShow.setImageResource(R.drawable.show);
                } else {
                    binding.passEdit.setInputType(129);
                    binding.imgShow.setImageResource(R.drawable.ic_hidden);
                }
            }
        });
    }
}