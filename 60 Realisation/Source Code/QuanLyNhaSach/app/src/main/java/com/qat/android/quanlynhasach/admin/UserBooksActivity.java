package com.qat.android.quanlynhasach.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.models.Cart;
import com.qat.android.quanlynhasach.view_holder.CartViewHolder;

public class UserBooksActivity extends AppCompatActivity {

    private RecyclerView mBooksList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_books);

        userID = getIntent().getStringExtra("uid");

        mBooksList = findViewById(R.id.recycler_menu_user_products);
        mBooksList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mBooksList.setLayoutManager(layoutManager);


        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userID).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, UserBooksViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, UserBooksViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull UserBooksViewHolder holder, int position, @NonNull Cart model) {

                holder.mTxtUserBookName.setText(model.getPname());
                holder.mTxtUserBookPrice.setText(model.getPrice() + "₫");
                holder.mTxtUserBookQuantity.setText(model.getQuantity());
            }

            @NonNull
            @Override
            public UserBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_books_layout, parent, false);
                UserBooksViewHolder holder = new UserBooksViewHolder(view);
                return holder;
            }
        };

        mBooksList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserBooksViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtUserBookName, mTxtUserBookPrice, mTxtUserBookQuantity;

        UserBooksViewHolder(View itemView) {
            super(itemView);

            mTxtUserBookName = itemView.findViewById(R.id.txt_user_books_name);
            mTxtUserBookPrice = itemView.findViewById(R.id.txt_user_books_price);
            mTxtUserBookQuantity = itemView.findViewById(R.id.txt_user_books_quantity);
        }
    }
}
