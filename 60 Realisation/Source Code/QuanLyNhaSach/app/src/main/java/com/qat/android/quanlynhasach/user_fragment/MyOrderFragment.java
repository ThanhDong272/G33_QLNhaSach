package com.qat.android.quanlynhasach.user_fragment;

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
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.admin.UserBooksActivity;
import com.qat.android.quanlynhasach.admin_fragment.NewOrdersFragment;
import com.qat.android.quanlynhasach.constants.Constants;
import com.qat.android.quanlynhasach.models.AdminOrders;
import com.qat.android.quanlynhasach.models.UserOrders;

import org.w3c.dom.Text;

import java.util.HashMap;

public class MyOrderFragment extends Fragment {

    private RecyclerView mOrdersList;
    private DatabaseReference myOrdersRef;
    private DatabaseReference userOrderRef;

    private TextView mTxtCheckOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("My Order");

        myOrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Constants.currentOnlineUser.getUsername());

        userOrderRef = FirebaseDatabase.getInstance().getReference().child("User Orders");

        mTxtCheckOrder = getActivity().findViewById(R.id.txt_no_order_yet);

        mOrdersList = getActivity().findViewById(R.id.recycler_menu_my_oder);
        mOrdersList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        checkOrder();

        FirebaseRecyclerOptions<UserOrders> options = new FirebaseRecyclerOptions.Builder<UserOrders>()
                .setQuery(myOrdersRef, UserOrders.class)
                .build();

        FirebaseRecyclerAdapter<UserOrders, MyOrderFragment.MyOrderViewHolder> adapter = new FirebaseRecyclerAdapter<UserOrders, MyOrderFragment.MyOrderViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull MyOrderFragment.MyOrderViewHolder holder, final int position, @NonNull final UserOrders model) {
                holder.mTxtMyOrderID.setText("Order ID: " + model.getOrderID());
                holder.mTxtMyOrderTotalPrice.setText("Total Price: " + model.getTotalPrice() + "₫");
                holder.mTxtMyOrderDateTime.setText("Order at: " + model.getDate() + "  " + model.getTime());
                holder.mTxtMyOderStateShip.setText("Status Ship: " + model.getState());

                holder.mBtnShowBooks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(getContext(), UserBooksActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

                holder.mBtnCancelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Do you want to cancel this order ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    String orderID = getRef(position).getKey();
                                    myOrdersRef.child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Cancer Order Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    userOrderRef.child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public MyOrderFragment.MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_oder_layout, parent, false);
                return new MyOrderFragment.MyOrderViewHolder(view);
            }
        };

        mOrdersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrder() {
        myOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    mTxtCheckOrder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static class MyOrderViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtMyOrderID, mTxtMyOrderTotalPrice, mTxtMyOderStateShip, mTxtMyOrderDateTime;
        Button mBtnShowBooks, mBtnCancelOrder;


        MyOrderViewHolder(View itemView) {
            super(itemView);

            mTxtMyOrderID = itemView.findViewById(R.id.txt_my_order_id);
            mTxtMyOrderTotalPrice = itemView.findViewById(R.id.txt_my_order_total_price);
            mTxtMyOderStateShip = itemView.findViewById(R.id.txt_my_order_state_ship);
            mTxtMyOrderDateTime = itemView.findViewById(R.id.txt_my_order_date_time);
            mBtnShowBooks = itemView.findViewById(R.id.btn_show_books);
            mBtnCancelOrder = itemView.findViewById(R.id.btn_cancel_order);
        }
    }
}
