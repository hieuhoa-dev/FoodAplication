package com.example.foodapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapp.Adapter.WaitConfirmAdapter;
import com.example.foodapp.Helper.BillRepository;
import com.example.foodapp.Helper.BillStatus;
import com.example.foodapp.Helper.OnBillListener;
import com.example.foodapp.Model.Bill;
import com.example.foodapp.R;
import com.example.foodapp.databinding.FragmentWaitDeliveryBinding;
import com.example.foodapp.databinding.FragmentWaitPaymentBinding;
import com.google.firebase.firestore.ListenerRegistration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class WaitPaymentFragment extends Fragment {

    FragmentWaitPaymentBinding binding;
    ListenerRegistration billListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaitPaymentBinding.inflate(getLayoutInflater());
        setVariable();
        return binding.getRoot();
    }
    void setVariable() {
        BillRepository billRepository = new BillRepository(getContext());
        ArrayList<Bill> billList = new ArrayList<>();
        WaitConfirmAdapter adapter = new WaitConfirmAdapter(billList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.waitPaymentRV.setLayoutManager(linearLayoutManager);
        binding.waitPaymentRV.setAdapter(adapter);
        binding.waitPaymentRV.setVisibility(View.GONE);
        binding.notFound.setVisibility(View.VISIBLE);
        billListener = billRepository.getBillOnChangeListener(new OnBillListener() {
            @Override
            public void onDataReceived(ArrayList<Bill> bills, BillStatus status) {
                if (status == BillStatus.WAIT_PAYMENT) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    billList.clear();
                    billList.addAll(bills);
                    adapter.notifyDataSetChanged();
                    binding.waitPaymentRV.setVisibility(View.VISIBLE);
                    binding.notFound.setVisibility(View.GONE);
                    Log.i("TAG", "WaitConfirm Bills received at " + now.format(formatter) + ": " + billList.toString());
                }
                // Bỏ qua các trạng thái khác nếu chỉ hiển thị WAIT_CONFIRM
            }

            @Override
            public void onDataEmpty() {
                billList.clear();
                adapter.notifyDataSetChanged();
                binding.waitPaymentRV.setVisibility(View.GONE);
                binding.notFound.setVisibility(View.VISIBLE);
                Log.i("TAG", "onDataEmpty");
            }

            @Override
            public void onError() {
                Log.e("TAG", "onError");
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        if (billListener != null) {
            billListener.remove();
            billListener = null;
        }
    }
}