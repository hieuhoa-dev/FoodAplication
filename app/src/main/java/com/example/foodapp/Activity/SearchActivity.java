package com.example.foodapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodapp.Adapter.SearchRecommendAdpater;
import com.example.foodapp.Helper.ManagementSearchRecent;
import com.example.foodapp.Model.searchRecommend;
import com.example.foodapp.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;
    private SearchRecommendAdpater adapter;
    private ArrayList<searchRecommend> recentList;
    private ManagementSearchRecent managementSearchRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thiết lập window insets (nếu cần)
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        managementSearchRecent = new ManagementSearchRecent(this);
        recentList = managementSearchRecent.getListRecent();
        initSearchRecommend();
        setVariable();
    }

    private void setVariable() {
        // Back button
        binding.backBtn.setOnClickListener(v -> finish());

        // Clear button: xóa nội dung và ẩn danh sách gợi ý
        binding.clearBtn.setOnClickListener(v -> {
            binding.searchEdit.setText("");
            binding.clearBtn.setVisibility(View.GONE);
            updateSearchRecommendations("");
        });

        // TextWatcher cho searchEdit để hiện/ẩn nút clear và cập nhật gợi ý
        binding.searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString().trim();
                if (!query.isEmpty()) {
                    binding.clearBtn.setVisibility(View.VISIBLE);
                } else {
                    binding.clearBtn.setVisibility(View.GONE);
                }
                updateSearchRecommendations(query);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // Xử lý nút search: khi nhấn search button
        binding.searchBtn.setOnClickListener(v -> performSearch());

        // Xử lý phím Enter trong searchEdit
        binding.searchEdit.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    // Phương thức thực hiện tìm kiếm: lưu từ khóa vào recent và chuyển sang ListFoodsActivity
    private void performSearch() {
        String text = binding.searchEdit.getText().toString().trim();
        if (!text.isEmpty()) {
            // Thêm vào recent (chỉ thêm nếu chưa có, bên trong ManagementSearchRecent tự xử lý)
            managementSearchRecent.insertSearchRecent(new searchRecommend(text, "Recent"));
            // Cập nhật danh sách gợi ý
            updateSearchRecommendations(text);
            // Chuyển sang màn hình kết quả
            Intent intent = new Intent(SearchActivity.this, ListFoodsActivity.class);
            intent.putExtra("searchText", text);
            intent.putExtra("isSearch", true);
            startActivity(intent);
        }
    }

    // Cập nhật danh sách gợi ý dựa trên query
    private void updateSearchRecommendations(String query) {
        // Nếu query rỗng, hiển thị tất cả recent; nếu không, lọc theo query
        ArrayList<searchRecommend> filteredList = new ArrayList<>();
        for (searchRecommend item : managementSearchRecent.getListRecent()) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (adapter != null) {
            adapter.updateData(filteredList);
        }
    }

    // Khởi tạo RecyclerView cho danh sách gợi ý
    void initSearchRecommend() {
        DatabaseReference myRef = database.getReference("searchRecommend");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Nếu có dữ liệu từ Firebase, thêm vào danh sách recent
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        searchRecommend item = issue.getValue(searchRecommend.class);
                        if (item != null) {
                            // Nếu chưa tồn tại, thêm vào danh sách recent
                            boolean exists = false;
                            for (searchRecommend r : recentList) {
                                if (r.getTitle().equalsIgnoreCase(item.getTitle())) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                recentList.add(item);
                            }
                        }
                    }
                }
                // Thiết lập RecyclerView với adapter
                binding.searchRecommendView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
                adapter = new SearchRecommendAdpater(recentList);
                binding.searchRecommendView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading search recommendations: " + error.getMessage());
            }
        });
    }
}
