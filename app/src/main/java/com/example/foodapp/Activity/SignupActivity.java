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
import com.example.foodapp.Helper.UserRepository;
import com.example.foodapp.Model.Users;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setVariable();

    }

    // Kiểm tra mật khẩu có hợp lệ hay không và đăng ký tài khoản
    private void setVariable() {
        binding.signupBtn.setOnClickListener(view -> {
            String email = binding.userEdit.getText().toString();
            String password = binding.passEdit.getText().toString();
            CustomDialog dialog = new CustomDialog(SignupActivity.this);

            if (email.isEmpty() || password.isEmpty()) {
                dialog.DialogNormal("Missing Info", "Please fill in all fields.");
                return;
            }

            if (password.length() < 6) {
                dialog.DialogNormal("Weak Password", "Your password must be at least 6 characters.");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Signup successful");
                            SetAccount();
                            dialog.DialogNormal("Success", "Sign up successfully.");
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Exception e = task.getException();

                            if (e instanceof FirebaseAuthUserCollisionException) {
                                dialog.DialogNormal("Account Exists", "This email is already registered.");
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                dialog.DialogNormal("Invalid Email", "Please enter a valid email address.");
                            } else if (e instanceof FirebaseNetworkException) {
                                dialog.DialogNormal("Network Error", "Please check your internet connection.");
                            } else {
                                dialog.DialogNormal("Signup Failed", "Error: " + e.getMessage());
                            }

                            Log.e(TAG, "Signup failed", e);
                        }
                    });
        });


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
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

    void SetAccount() {
        UserRepository userRepository = new UserRepository();
        String uid = FirebaseAuth.getInstance().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String name = binding.nameEdit.getText().toString();
        Users user = new Users(uid, name, "phoneNumber", email,"","");
        userRepository.setUser(user);
    }
}