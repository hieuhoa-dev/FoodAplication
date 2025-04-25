package com.example.foodapp.Components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodapp.Adapter.BillDetailsAdapter;

import com.example.foodapp.Model.Foods;
import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import java.util.List;


public class BottomSheetDialog extends BottomSheetDialogFragment {

    private final Context context;
    private FragmentBottomSheetBinding binding;

    private List<Foods> foods;

    public BottomSheetDialog(List<Foods> foods, Context context) {
        this.context = context;
        this.foods = foods;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        BillDetailsAdapter adapter = new BillDetailsAdapter(foods,context);
        binding.recyclerView.setAdapter(adapter);


        // Thiết lập nút đóng
        ImageButton closeButton = binding.closeButton;
        closeButton.setOnClickListener(v -> dismiss());

        // Thiết lập BottomSheetBehavior
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(binding.bottomSheetContent);
        behavior.setSkipCollapsed(true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setFitToContents(true);
        behavior.setExpandedOffset(0);
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss(); // Đóng khi ẩn
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Xử lý hiệu ứng khi kéo (nếu cần)
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
