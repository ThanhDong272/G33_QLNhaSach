package com.qat.android.quanlynhasach.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qat.android.quanlynhasach.ItemClickListener;
import com.qat.android.quanlynhasach.R;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtBookName, txtBookPrice;
    public ImageView imageView;
    public ItemClickListener listener;

    public BookViewHolder(View itemView)
    {
        super(itemView);

        imageView = itemView.findViewById(R.id.img_book_item);
        txtBookName = itemView.findViewById(R.id.txt_book_item_name);
        txtBookPrice = itemView.findViewById(R.id.txt_book_item_price);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
