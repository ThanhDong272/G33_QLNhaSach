package com.qat.android.quanlynhasach.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qat.android.quanlynhasach.R;

public class PaymentMethodActivity extends AppCompatActivity {

    private Button mBtnPayWithCash;

    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        mBtnPayWithCash = findViewById(R.id.btn_pay_with_cash);

        mBtnPayWithCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentMethodActivity.this, ConfirmOrderActivity.class);
                startActivity(intent);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
            }
        });
    }
}
