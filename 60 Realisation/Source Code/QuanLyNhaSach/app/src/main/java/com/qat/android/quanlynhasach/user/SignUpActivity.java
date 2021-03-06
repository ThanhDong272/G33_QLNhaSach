package com.qat.android.quanlynhasach.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.qat.android.quanlynhasach.CheckConnection;
import com.qat.android.quanlynhasach.R;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEditTextUsername, mEditTextPassword, mEditTextConfirmPassword;
    private MyLoadingButton mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEditTextUsername = findViewById(R.id.edit_username);
        mEditTextPassword = findViewById(R.id.edit_password);
        mEditTextConfirmPassword = findViewById(R.id.edit_confirm_password);
        mBtnSignUp = findViewById(R.id.btn_signup);

        mBtnSignUp.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String confirmPassword = mEditTextConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(username) || mEditTextUsername.length() <= 5 || mEditTextUsername.length() >= 16) {
            Toast.makeText(this, "Username must be 6 to 15 characters", Toast.LENGTH_SHORT).show();
            mBtnSignUp.showNormalButton();
        } else if (TextUtils.isEmpty(password) || mEditTextPassword.length() <= 5 || mEditTextPassword.length() >= 16) {
            Toast.makeText(this, "Password must be 6 to 15 characters", Toast.LENGTH_SHORT).show();
            mBtnSignUp.showNormalButton();
        } else if (TextUtils.isEmpty(confirmPassword) || mEditTextConfirmPassword.length() <= 5 || mEditTextConfirmPassword.length() >= 16) {
            Toast.makeText(this, "Confirm password must be 6 to 15 characters", Toast.LENGTH_SHORT).show();
            mBtnSignUp.showNormalButton();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Confirm password doesn't match", Toast.LENGTH_SHORT).show();
            mBtnSignUp.showNormalButton();
        } else if (CheckConnection.isOnline(SignUpActivity.this)) {
            validateUsername(username, password, confirmPassword);
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            mBtnSignUp.showNormalButton();
        }
    }

    private void validateUsername(final String username, final String password, final String phone) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(username).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);

                    RootRef.child("Users").child(username).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                mBtnSignUp.showNormalButton();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "This " + "username " + username + " is already exists.", Toast.LENGTH_SHORT).show();
                    mBtnSignUp.showNormalButton();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
