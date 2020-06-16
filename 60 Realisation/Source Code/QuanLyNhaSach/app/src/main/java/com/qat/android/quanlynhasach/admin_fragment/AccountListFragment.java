package com.qat.android.quanlynhasach.admin_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qat.android.quanlynhasach.CheckConnection;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.admin.DetailAccountActivity;
import com.qat.android.quanlynhasach.constants.Constants;
import com.qat.android.quanlynhasach.models.Cart;
import com.qat.android.quanlynhasach.models.Users;
import com.qat.android.quanlynhasach.user.BookDetailActivity;
import com.qat.android.quanlynhasach.view_holder.AccountListViewHolder;
import com.qat.android.quanlynhasach.view_holder.CartViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountListFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private TextView mTxtCheckAccount;

    private DatabaseReference accountListRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_account_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountListRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mTxtCheckAccount = getActivity().findViewById(R.id.txt_no_account_yet);

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Account List");

        recyclerView = getActivity().findViewById(R.id.recycler_menu_account_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();

        checkAccount();

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(accountListRef, Users.class)
                .build();

        FirebaseRecyclerAdapter<Users, AccountListViewHolder> adapter = new FirebaseRecyclerAdapter<Users, AccountListViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull AccountListViewHolder holder, int position, @NonNull final Users model) {
                Picasso.get().load(model.getImage()).into(holder.mCircleImage);
                holder.mTxtUsername.setText(model.getUsername());
                holder.mTxtPhoneNumber.setText(model.getPhone());
                holder.mTxtAddress.setText(model.getAddress());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (CheckConnection.isOnline(getContext())) {
                            String uID = getRef(position).getKey();
                            Intent intent = new Intent(getContext(), DetailAccountActivity.class);
                            intent.putExtra("userID", uID);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @NonNull
            @Override
            public AccountListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_list_layout, parent, false);
                AccountListViewHolder holder = new AccountListViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkAccount() {
        accountListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    mTxtCheckAccount.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
