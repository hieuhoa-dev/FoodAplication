package com.example.foodapp.Helper;

import static com.example.foodapp.Activity.BaseActivity.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.foodapp.Model.Bill;
import com.example.foodapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BillRepository {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseFirestore firestore;
    Context context;

    public BillRepository(Context context) {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public void SaveBill(Bill bill) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        firestore.collection("Bill")
                .add(bill)
                .addOnSuccessListener(documentReference -> {
                    String billId = documentReference.getId(); // Lấy ID tự động
                    documentReference.update("id", billId) // Cập nhật id
                            .addOnSuccessListener(e ->
                                    Toast.makeText(context, "Order successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(context, "Failed to update bill ID", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Order fail", Toast.LENGTH_SHORT).show();
                });
    }

    public ListenerRegistration getBillOnChangeListener(OnBillListener getBill) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("Bill").whereEqualTo("idUser", uid);

        // Lắng nghe thay đổi thời gian thực
        return query.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.e(TAG, "Error listening to bill info", e);
                getBill.onError();
                return;
            }

            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                // Tạo danh sách riêng cho từng trạng thái
                ArrayList<Bill> waitConfirmBills = new ArrayList<>();
                ArrayList<Bill> waitDeliveryBills = new ArrayList<>();
                ArrayList<Bill> waitPaymentBills = new ArrayList<>();

                // Phân loại hóa đơn theo trạng thái
                for (QueryDocumentSnapshot document : querySnapshot) {
                    try {
                        Bill bill = document.toObject(Bill.class);
                        bill.setId(document.getId());
                        Log.i(TAG, "Bill data received: " + bill.getId());

                        // Ánh xạ status từ chuỗi mô tả sang BillStatus
                        BillStatus status = BillStatus.fromDescription(bill.getStatus());
                        switch (status) {
                            case WAIT_CONFIRM:
                                waitConfirmBills.add(bill);
                                break;
                            case WAIT_DELIVERY:
                                waitDeliveryBills.add(bill);
                                break;
                            case WAIT_PAYMENT:
                                waitPaymentBills.add(bill);
                                break;
                            default:
                                Log.w(TAG, "Unknown status for bill: " + bill.getId());
                        }
                    } catch (IllegalArgumentException ex) {
                        Log.e(TAG, "Invalid status for bill: " + document.getId(), ex);
                        getBill.onError();
                    } catch (Exception ex) {
                        Log.e(TAG, "Error deserializing bill: " + document.getId(), ex);
                        getBill.onError();
                    }
                }

                // Gọi callback cho từng trạng thái nếu danh sách không rỗng
                if (!waitConfirmBills.isEmpty()) {
                    getBill.onDataReceived(waitConfirmBills, BillStatus.WAIT_CONFIRM);
                }
                if (!waitDeliveryBills.isEmpty()) {
                    getBill.onDataReceived(waitDeliveryBills, BillStatus.WAIT_DELIVERY);
                }
                if (!waitPaymentBills.isEmpty()) {
                    getBill.onDataReceived(waitPaymentBills, BillStatus.WAIT_PAYMENT);
                }

                // Nếu không có hóa đơn nào trong các trạng thái mong muốn
                if (waitConfirmBills.isEmpty() && waitDeliveryBills.isEmpty() && waitPaymentBills.isEmpty()) {
                    getBill.onDataEmpty();
                }
            } else {
                Log.w(TAG, "No bills found");
                getBill.onDataEmpty();
            }
        });
    }
}
