package com.qat.android.quanlynhasach.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qat.android.quanlynhasach.R;

public class AccountListViewHolder extends RecyclerView.ViewHolder {

    public TextView mTxtUsername, mTxtPassword, mTxtFullName,
            mTxtSex, mTxtPhoneNumber, mTxtEmail, mTxtAddress;

    public AccountListViewHolder(@NonNull View itemView) {
        super(itemView);

        mTxtUsername = itemView.findViewById(R.id.txt_cart_user_name);
        mTxtPassword = itemView.findViewById(R.id.txt_cart_password);
        mTxtFullName = itemView.findViewById(R.id.txt_cart_full_name);
        mTxtSex = itemView.findViewById(R.id.txt_cart_sex);
        mTxtPhoneNumber = itemView.findViewById(R.id.txt_cart_phone_number);
        mTxtEmail = itemView.findViewById(R.id.txt_cart_email);
        mTxtAddress = itemView.findViewById(R.id.txt_cart_address);
    }
}
