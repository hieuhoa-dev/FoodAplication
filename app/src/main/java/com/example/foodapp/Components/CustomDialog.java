package com.example.foodapp.Components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.foodapp.Activity.LoginActivity;
import com.example.foodapp.R;


public class CustomDialog {
    private final Dialog dialog;
    private final TextView titleView, messageView;
    private final Button cancelBtn, okBtn;
    private final Context context;

    public CustomDialog(Context context) {
        dialog = new Dialog(context);
        this.context = context;

        dialog.setContentView(R.layout.custom_dialog);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        titleView = dialog.findViewById(R.id.title_dialog);
        messageView = dialog.findViewById(R.id.message_dialog);
        cancelBtn = dialog.findViewById(R.id.cancelbtn_dialog);
        okBtn = dialog.findViewById(R.id.okbtn_dialog);
    }

    public void DialogNormal( String title,String message) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
        dialog.setHidenCancel();
        dialog.setOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setMessage(String message) {
        messageView.setText(message);
    }

    public void setCancelClickListener(View.OnClickListener listener) {
        cancelBtn.setOnClickListener(listener);
    }

    public void setOkClickListener(View.OnClickListener listener) {
        okBtn.setOnClickListener(listener);
    }

    public void setHidenCancel()
    {
        cancelBtn.setVisibility(View.GONE);
    }
    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
