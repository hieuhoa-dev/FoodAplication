package com.example.foodapp.Activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivitySearchBinding;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;

    private ArrayAdapter<String> searchAdapter;
    private List<String> searchHistory;  // Lưu tạm trong bộ nhớ
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "search_pref";
    private static final String KEY_HISTORY = "search_history";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.getRoot().getId()), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.searchEdit.getText().toString();
                if (!text.isEmpty()) {
                    Intent itent = new Intent(SearchActivity.this, ListFoodsActivity.class);
                    itent.putExtra("searchText", text);
                    itent.putExtra("isSearch", true);
                    startActivity(itent);
                }
            }
        });
        binding.searchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_ENTER) {
                        String text = binding.searchEdit.getText().toString();
                        if (!text.isEmpty()) {
                            Intent itent = new Intent(SearchActivity.this, ListFoodsActivity.class);
                            itent.putExtra("searchText", text);
                            itent.putExtra("isSearch", true);
                            startActivity(itent);
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        binding.searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    binding.clearBtn.setVisibility(View.VISIBLE);
                } else {
                    binding.clearBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchEdit.clearFocus();
                binding.searchEdit.setText("");
                binding.clearBtn.setVisibility(View.GONE);
            }
        });
    }
}
