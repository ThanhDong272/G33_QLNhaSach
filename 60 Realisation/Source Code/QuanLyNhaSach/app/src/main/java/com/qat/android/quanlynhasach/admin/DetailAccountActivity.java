package com.qat.android.quanlynhasach.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.constants.Constants;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailAccountActivity extends AppCompatActivity {

    private String userID = "";

    private TextView mTxtDetailUsername, mTxtDetailPassword, mTxtDetailFullName,
            mTxtDetailSex, mTxtDetailPhone, mTxtDetailEmail, mTxtDetailAddress;
    private CircleImageView mCircleImgDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_account);

        userID = getIntent().getStringExtra("userID");

        mTxtDetailUsername = findViewById(R.id.txt_detail_account_username);
        mTxtDetailPassword = findViewById(R.id.txt_detail_account_password);
        mTxtDetailFullName = findViewById(R.id.txt_detail_account_fullname);
        mTxtDetailSex = findViewById(R.id.txt_detail_account_sex);
        mTxtDetailPhone = findViewById(R.id.txt_detail_account_phone);
        mTxtDetailEmail = findViewById(R.id.txt_detail_account_email);
        mTxtDetailAddress = findViewById(R.id.txt_detail_account_address);

        mCircleImgDetail = findViewById(R.id.img_detail_account);

        displayDetailAccount();
    }

    private void displayDetailAccount() {

        DatabaseReference accountDetailRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        accountDetailRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("image").exists()) {
                    String username = dataSnapshot.child("username").getValue().toString();
                    String password = dataSnapshot.child("password").getValue().toString();
                    String fullName = dataSnapshot.child("fullName").getValue().toString();
                    String sex = dataSnapshot.child("sex").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String address = dataSnapshot.child("address").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    mTxtDetailUsername.setText(username);
                    mTxtDetailPassword.setText(password);
                    mTxtDetailFullName.setText(fullName);
                    mTxtDetailSex.setText(sex);
                    mTxtDetailPhone.setText(phone);
                    mTxtDetailEmail.setText(email);
                    mTxtDetailAddress.setText(address);
                    Picasso.get().load(image).into(mCircleImgDetail);
                } else {
                    String username = dataSnapshot.child("username").getValue().toString();
                    String password = dataSnapshot.child("password").getValue().toString();
                    String fullName = dataSnapshot.child("fullName").getValue().toString();
                    String sex = dataSnapshot.child("sex").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String address = dataSnapshot.child("address").getValue().toString();

                    mTxtDetailUsername.setText(username);
                    mTxtDetailPassword.setText(password);
                    mTxtDetailFullName.setText(fullName);
                    mTxtDetailSex.setText(sex);
                    mTxtDetailPhone.setText(phone);
                    mTxtDetailEmail.setText(email);
                    mTxtDetailAddress.setText(address);
                    Picasso.get().load(R.drawable.img_profile).into(mCircleImgDetail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
