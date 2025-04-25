package com.example.foodapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodapp.Fragment.WaitConfirmFragment;
import com.example.foodapp.Fragment.WaitDeliveryFragment;
import com.example.foodapp.Fragment.WaitPaymentFragment;

public class MangerOrderAdapter extends FragmentStateAdapter {
    public MangerOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WaitConfirmFragment();
            case 1:
                return new WaitPaymentFragment();
            case 2:
                return new WaitDeliveryFragment();
            default:
                return new WaitConfirmFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
