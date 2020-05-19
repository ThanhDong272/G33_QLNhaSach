package com.qat.android.quanlynhasach.admin_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.admin.MaintainBookActivity;
import com.qat.android.quanlynhasach.models.Products;
import com.qat.android.quanlynhasach.view_holder.BookViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class HomeAdminFragment extends Fragment {

    private DatabaseReference ProductsRef;
    private RecyclerView mRecyclerView;

    private String type = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getActivity().getIntent().getExtras().get("Admins").toString();
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(getContext());

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Home");

        mRecyclerView = getActivity().findViewById(R.id.recycler_menu_item);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, BookViewHolder> adapter = new FirebaseRecyclerAdapter<Products, BookViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull final Products model) {
                holder.txtBookName.setText(model.getPname());
                holder.txtBookPrice.setText(model.getPrice() + "₫");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (type.equals("Admins")) {
//
//                        } else {
//                            Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
//                            intent.putExtra("pid", model.getPid());
//                            startActivity(intent);
//                        }
                        Intent intent = new Intent(getContext(), MaintainBookActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_layout, parent, false);
                BookViewHolder holder = new BookViewHolder(view);
                return holder;
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}