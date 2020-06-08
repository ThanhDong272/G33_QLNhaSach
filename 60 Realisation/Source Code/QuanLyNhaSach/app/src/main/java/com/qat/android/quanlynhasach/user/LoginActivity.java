package com.qat.android.quanlynhasach.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qat.android.quanlynhasach.CheckConnection;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.admin.MainAdminActivity;
import com.qat.android.quanlynhasach.constants.Constants;
import com.qat.android.quanlynhasach.models.Users;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextUsername, mEditTextPassword;
    private MyLoadingButton mBtnLogin;
    private TextView mTxtNoAccount;
    private CheckBox mCheckBoxRememberMe;
    private TextView mTxtAdmin, mTxtNotAdmin;

    private String parentDbName = "Users";

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextUsername = findViewById(R.id.edit_username);
        mEditTextPassword = findViewById(R.id.edit_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mTxtNoAccount = findViewById(R.id.txt_noAccount);
        mCheckBoxRememberMe = findViewById(R.id.checkbox_rememberme);
        Paper.init(this);

        mTxtAdmin = findViewById(R.id.txt_admin);
        mTxtNotAdmin = findViewById(R.id.txt_not_admin);

        loadingBar = new ProgressDialog(this);

        mTxtNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mBtnLogin.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                LoginUser();
            }
        });

        String UserNameKey = Paper.book().read(Constants.USER_NAME_KEY);
        String UserPasswordKey = Paper.book().read(Constants.USER_PASSWORD_KEY);

        if (UserNameKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserNameKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                if (CheckConnection.isOnline(LoginActivity.this)) {
                    AllowAccess(UserNameKey, UserPasswordKey);
                    loadingBar.setTitle("You're already logged in !");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                } else {
                    Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
        }

        mTxtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnLogin.setButtonLabel("Login Admin");
                mTxtAdmin.setVisibility(View.INVISIBLE);
                mTxtNotAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        mTxtNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnLogin.setButtonLabel("Login");
                mTxtAdmin.setVisibility(View.VISIBLE);
                mTxtNotAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void LoginUser() {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();

        if (TextUtils.isEmpty(username) || mEditTextUsername.length() <= 5 || mEditTextUsername.length() >= 16) {
            Toast.makeText(this, "Username must be 6 to 15 characters", Toast.LENGTH_SHORT).show();
            mBtnLogin.showNormalButton();
        } else if (TextUtils.isEmpty(password) || mEditTextPassword.length() <= 5 || mEditTextPassword.length() >= 16) {
            Toast.makeText(this, "Password must be 6 to 15 characters", Toast.LENGTH_SHORT).show();
            mBtnLogin.showNormalButton();
        } else if (CheckConnection.isOnline(LoginActivity.this)) {
            AllowAccessToAccount(username, password);
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            mBtnLogin.showNormalButton();
        }
    }

    private void AllowAccessToAccount(final String username, final String password) {
        if (mCheckBoxRememberMe.isChecked()) {
            Paper.book().write(Constants.USER_NAME_KEY, username);
            Paper.book().write(Constants.USER_PASSWORD_KEY, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(username).exists()) {
                    Users usersData = dataSnapshot.child(parentDbName).child(username).getValue(Users.class);

                    if (usersData.getUsername().equals(username)) {
                        if (usersData.getPassword().equals(password)) {
                            if (parentDbName.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in successfully", Toast.LENGTH_SHORT).show();
                                mBtnLogin.showNormalButton();
                                Intent intent = new Intent(LoginActivity.this, MainAdminActivity.class);
                                startActivity(intent);
                            } else if (parentDbName.equals("Users")) {
                                mBtnLogin.showNormalButton();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Constants.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        } else {
                            mBtnLogin.showNormalButton();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Username " + username + " does not exist.", Toast.LENGTH_SHORT).show();
                    mBtnLogin.showNormalButton();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AllowAccess(final String username, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(username).exists()) {
                    Users usersData = dataSnapshot.child("Users").child(username).getValue(Users.class);

                    if (usersData.getUsername().equals(username)) {
                        if (usersData.getPassword().equals(password)) {
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Constants.currentOnlineUser = usersData;
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Username " + username + " does not exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
