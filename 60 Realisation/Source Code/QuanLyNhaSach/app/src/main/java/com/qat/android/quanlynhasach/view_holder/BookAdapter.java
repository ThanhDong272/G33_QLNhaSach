package com.qat.android.quanlynhasach.view_holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.models.Books;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private ArrayList<Books> mArrayListBook;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public BookAdapter(ArrayList<Books> mArrayListBook) {
        this.mArrayListBook = mArrayListBook;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book_layout, viewGroup, false);
        return new BookViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.mTxtBookName.setText(mArrayListBook.get(position).getPname());
        holder.mTxtBookPrice.setText(mArrayListBook.get(position).getPrice());
        Picasso.get().load(mArrayListBook.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mArrayListBook.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtBookName, mTxtBookPrice;
        ImageView imageView;

        public BookViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_book_item);
            mTxtBookName = itemView.findViewById(R.id.txt_book_item_name);
            mTxtBookPrice = itemView.findViewById(R.id.txt_book_item_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
