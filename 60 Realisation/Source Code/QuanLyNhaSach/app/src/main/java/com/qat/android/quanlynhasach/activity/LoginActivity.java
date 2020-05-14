package com.qat.android.quanlynhasach.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myloadingbutton.MyLoadingButton;
import com.qat.android.quanlynhasach.R;

public class LoginActivity extends AppCompatActivity {

    private MyLoadingButton LoginButton;
    private TextView NoAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.btn_login);
        NoAccount = findViewById(R.id.txt_noAccount);

        getSupportActionBar().hide();

        LoginButton.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {

            }
        });

        NoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
