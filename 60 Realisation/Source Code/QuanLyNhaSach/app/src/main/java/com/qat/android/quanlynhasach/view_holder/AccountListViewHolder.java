package com.qat.android.quanlynhasach.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qat.android.quanlynhasach.ItemClickListener;
import com.qat.android.quanlynhasach.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mTxtUsername, mTxtPhoneNumber, mTxtAddress;
    public CircleImageView mCircleImage;
    public ItemClickListener listener;

    public AccountListViewHolder(@NonNull View itemView) {
        super(itemView);

        mCircleImage = itemView.findViewById(R.id.img_account);
        mTxtUsername = itemView.findViewById(R.id.txt_list_user_name);
        mTxtPhoneNumber = itemView.findViewById(R.id.txt_list_phone);
        mTxtAddress = itemView.findViewById(R.id.txt_list_address);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
