package com.qat.android.quanlynhasach.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEditTextUsername, mEditTextPhone, mEditTextPassword;
    private MyLoadingButton mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEditTextUsername = findViewById(R.id.edit_username);
        mEditTextPhone = findViewById(R.id.edit_phone);
        mEditTextPassword = findViewById(R.id.edit_password);
        mBtnSignUp = findViewById(R.id.btn_signup);

        mBtnSignUp.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String phone = mEditTextPhone.getText().toString();

        if (TextUtils.isEmpty(username) || mEditTextUsername.length() <= 5 || mEditTextUsername.length() >= 16) {
            Toast.makeText(this, "Username must be 6 to 15 characters", Toast.LENGTH_SHORT).show();
            mBtnSignUp.showNormalButton();
        } else if (TextUtils.isEmpty(password) || mEditTextPassword.length() <= 5 || mEditTextPassword.length() >= 16) {
            Toast.makeText(this, "Password must be 6 to 15 characters", Toast.LENGTH_SHORT).show();
            mBtnSignUp.showNormalButton();
        } else if (TextUtils.isEmpty(phone) || mEditTextPhone.length() <= 6 || mEditTextPhone.length() >= 12) {
            Toast.makeText(this, "Phone number must be 7 to 11 characters", Toast.LENGTH_SHORT).show();
            mBtnSignUp.showNormalButton();
        } else {
            ValidatephoneNumber(username, password, phone);
        }
    }

    private void ValidatephoneNumber(final String username, final String password, final String phone) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(username).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);
                    userdataMap.put("phone", phone);

                    RootRef.child("Users").child(username).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                mBtnSignUp.showNormalButton();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                mBtnSignUp.showNormalButton();
                                Toast.makeText(SignUpActivity.this, "Network Error: Please try again after some time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "This " + "username " + username + " is already exists.", Toast.LENGTH_SHORT).show();
                    mBtnSignUp.showNormalButton();
                    Toast.makeText(SignUpActivity.this, "Please try again using another username.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
