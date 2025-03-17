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

import com.example.foodapp.Model.Users;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

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
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.userEdit.getText().toString();
                String password = binding.passEdit.getText().toString();
                if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Your password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Sẽ Sửa sau
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Successful");
                            SetAccount();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            Toast.makeText(SignupActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "Fail", task.getException());
                            Toast.makeText(SignupActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
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
                    binding.imgShow.setImageResource(R.drawable.hidden);
                }
            }
        });
    }

    void SetAccount() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Người dùng chưa đăng nhập, xử lý tùy ý
            return;
        }
        String uid = currentUser.getUid();
        String email = currentUser.getEmail();
        Users user = new Users(uid, "nameUser", "phoneNumber", email);

        // Lưu vào collection "users", document = uid
        firestore.collection("Users")
                .document(uid)           // Đặt doc ID = UID
                .set(user)              // Dùng set() thay vì add()
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "User info saved successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error saving user info", e);
                });
    }
}