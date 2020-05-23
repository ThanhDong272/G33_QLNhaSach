package com.qat.android.quanlynhasach.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qat.android.quanlynhasach.ItemClickListener;
import com.qat.android.quanlynhasach.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mTxtBookName, mTxtBookPrice, mTxtBookQuantity;
    public ImageView mImgBook, mImgClose;
    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);

        mImgBook = itemView.findViewById(R.id.img_cart_book);
        mImgClose = itemView.findViewById(R.id.img_close);
        mTxtBookName = itemView.findViewById(R.id.txt_cart_book_name);
        mTxtBookPrice = itemView.findViewById(R.id.txt_cart_book_price);
        mTxtBookQuantity = itemView.findViewById(R.id.txt_cart_book_quantity);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
