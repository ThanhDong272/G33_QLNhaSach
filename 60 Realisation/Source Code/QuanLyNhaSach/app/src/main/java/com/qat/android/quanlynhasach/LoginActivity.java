package com.qat.android.quanlynhasach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.myloadingbutton.MyLoadingButton;

public class LoginActivity extends AppCompatActivity {

    private MyLoadingButton LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.btn_login);


        getSupportActionBar().hide();

        LoginButton.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {

            }
        });
    }
}
