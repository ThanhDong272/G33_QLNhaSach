package com.qat.android.quanlynhasach.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.constants.Constants;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {

    private EditText mEditTextFullName, mEditTextPhone, mEditTextAddress, mEditTextEmail;
    private MyLoadingButton mBtnConfirmOrder;

    private String totalPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        totalPrice = getIntent().getStringExtra("Total Price");

        displayUserInfo();

        mEditTextFullName = findViewById(R.id.edit_confirm_order_full_name);
        mEditTextPhone = findViewById(R.id.edit_confirm_order_phone_number);
        mEditTextAddress = findViewById(R.id.edit_confirm_oder_address);
        mEditTextEmail = findViewById(R.id.edit_confirm_order_email);

        mBtnConfirmOrder = findViewById(R.id.btn_confirm_order);

        mBtnConfirmOrder.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                Check();
            }
        });
    }

    private void displayUserInfo() {

        DatabaseReference userInfoRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Constants.currentOnlineUser.getUsername());

        userInfoRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String fullName = dataSnapshot.child("fullName").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        mEditTextFullName.setText(fullName);
                        mEditTextPhone.setText(phone);
                        mEditTextAddress.setText(address);
                        mEditTextEmail.setText(email);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Check() {
        if (TextUtils.isEmpty(mEditTextFullName.getText().toString()) || mEditTextFullName.length() <= 5 || mEditTextFullName.length() >= 51) {
            Toast.makeText(ConfirmOrderActivity.this, "Full name must be 6 to 50 characters", Toast.LENGTH_SHORT).show();
            mBtnConfirmOrder.showNormalButton();
        } else if (TextUtils.isEmpty(mEditTextPhone.getText().toString()) || mEditTextPhone.length() <= 6 || mEditTextPhone.length() >= 12) {
            Toast.makeText(ConfirmOrderActivity.this, "Phone number must be 7 to 11 characters", Toast.LENGTH_SHORT).show();
            mBtnConfirmOrder.showNormalButton();
        } else if (TextUtils.isEmpty(mEditTextAddress.getText().toString()) || mEditTextAddress.length() <= 9 || mEditTextAddress.length() >= 101) {
            Toast.makeText(ConfirmOrderActivity.this, "Address must be 10 to 100 characters", Toast.LENGTH_SHORT).show();
            mBtnConfirmOrder.showNormalButton();
        } else if (TextUtils.isEmpty(mEditTextEmail.getText().toString()) || mEditTextEmail.length() <= 9 || mEditTextEmail.length() >= 51) {
            Toast.makeText(ConfirmOrderActivity.this, "Email must be 10 to 50 characters", Toast.LENGTH_SHORT).show();
            mBtnConfirmOrder.showNormalButton();
        } else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String mSaveCurrentDate, mSaveCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        mSaveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a");
        mSaveCurrentTime = currentTime.format(calendar.getTime());

        String key = FirebaseDatabase.getInstance().getReference().child("User Orders").child(Constants.currentOnlineUser.getUsername()).push().getKey();

        final DatabaseReference userOrdersRef = FirebaseDatabase.getInstance().getReference()
                .child("User Orders")
                .child(key);

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Constants.currentOnlineUser.getUsername())
                .child(key);

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("orderID", key);
        ordersMap.put("totalPrice", totalPrice);
        ordersMap.put("username", Constants.currentOnlineUser.getUsername());
        ordersMap.put("fullName", mEditTextFullName.getText().toString());
        ordersMap.put("phone", mEditTextPhone.getText().toString());
        ordersMap.put("address", mEditTextAddress.getText().toString());
        ordersMap.put("email", mEditTextEmail.getText().toString());
        ordersMap.put("date", mSaveCurrentDate);
        ordersMap.put("time", mSaveCurrentTime);
        ordersMap.put("state", "not shipped");

        userOrdersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Constants.currentOnlineUser.getUsername())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmOrderActivity.this, "Order Success", Toast.LENGTH_SHORT).show();
                                        mBtnConfirmOrder.showNormalButton();
                                        Intent intent = new Intent(ConfirmOrderActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }


}
