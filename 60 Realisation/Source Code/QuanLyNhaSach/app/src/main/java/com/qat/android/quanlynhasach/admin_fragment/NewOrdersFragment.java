package com.qat.android.quanlynhasach.admin_fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.admin.UserBooksActivity;
import com.qat.android.quanlynhasach.constants.Constants;
import com.qat.android.quanlynhasach.models.AdminOrders;

import java.util.HashMap;

public class NewOrdersFragment extends Fragment {

    private RecyclerView mOrdersList;
    private DatabaseReference userOrdersRef;

    private DatabaseReference ordersRef;

    private TextView mTxtCheckNewOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_new_orders, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("New Orders");

        userOrdersRef = FirebaseDatabase.getInstance().getReference().child("User Orders");

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Constants.currentOnlineUser.getUsername());

        mTxtCheckNewOrder = getActivity().findViewById(R.id.txt_no_new_order_yet);

        mOrdersList = getActivity().findViewById(R.id.recycler_menu_new_orders);
        mOrdersList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        checkOrder();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(userOrdersRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                        holder.mTxtOrderID.setText("Order ID: " + model.getOrderID());
                        holder.mTxtFullName.setText("Full Name: " + model.getFullName());
                        holder.mTxtUsername.setText("Username: " + model.getUsername());
                        holder.mTxtPhone.setText("Phone: " + model.getPhone());
                        holder.mTxtTotalPrice.setText("Total Price: " + model.getTotalPrice() + "₫");
                        holder.mTxtAddress.setText("Addess: " + model.getAddress());
                        holder.mTxtDateTime.setText("Order at: " + model.getDate() + "  " + model.getTime());
                        holder.mTxtStateShip.setText("Status Ship: " + model.getState());

                        holder.mBtnShowOrders.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(getContext(), UserBooksActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Do you want to ship this order ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            String orderID = getRef(position).getKey();
                                            HashMap<String, Object> newOrders = new HashMap<>();
                                            newOrders.put("state", "shipped");
                                            userOrdersRef.child(orderID).updateChildren(newOrders);
                                            ordersRef.child(orderID).updateChildren(newOrders);
                                        }
                                        else {
                                            String orderID = getRef(position).getKey();
                                            HashMap<String, Object> newOrders = new HashMap<>();
                                            newOrders.put("state", "not shipped");
                                            userOrdersRef.child(orderID).updateChildren(newOrders);
                                            ordersRef.child(orderID).updateChildren(newOrders);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_orders_layout, parent, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };

        mOrdersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrder() {
        userOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    mTxtCheckNewOrder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtOrderID, mTxtFullName, mTxtUsername, mTxtPhone,
                mTxtTotalPrice, mTxtAddress, mTxtStateShip, mTxtDateTime;
        Button mBtnShowOrders;


        AdminOrdersViewHolder(View itemView) {
            super(itemView);

            mTxtOrderID = itemView.findViewById(R.id.txt_order_id);
            mTxtFullName = itemView.findViewById(R.id.txt_order_full_name);
            mTxtUsername = itemView.findViewById(R.id.txt_order_username);
            mTxtPhone = itemView.findViewById(R.id.txt_order_phone_number);
            mTxtTotalPrice = itemView.findViewById(R.id.txt_order_total_price);
            mTxtAddress = itemView.findViewById(R.id.txt_order_address);
            mTxtStateShip = itemView.findViewById(R.id.txt_order_state_ship);
            mTxtDateTime = itemView.findViewById(R.id.txt_order_date_time);
            mBtnShowOrders = itemView.findViewById(R.id.btn_show_all_books);
        }
    }
}
