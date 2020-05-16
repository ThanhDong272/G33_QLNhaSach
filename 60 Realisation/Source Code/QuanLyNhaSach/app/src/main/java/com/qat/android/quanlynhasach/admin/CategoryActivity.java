package com.qat.android.quanlynhasach.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.activity.LoginActivity;
import com.qat.android.quanlynhasach.activity.MainActivity;

public class CategoryActivity extends AppCompatActivity {

    private Button mBtnMaintainProducts, mBtnCheckNewOrders, mBtnLogout;
    private ImageView mImgScience, mImgText, mImgChildren, mImgForeignLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        mImgScience = findViewById(R.id.img_science_book);
        mImgText = findViewById(R.id.img_text_book);
        mImgChildren = findViewById(R.id.img_kid_book);
        mImgForeignLanguage = findViewById(R.id.img_foreign_language_book);

        mBtnMaintainProducts = findViewById(R.id.btn_maintain_products);
        mBtnCheckNewOrders = findViewById(R.id.btn_check_new_orders);
        mBtnLogout = findViewById(R.id.btn_logout);

//        mBtnMaintainProducts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
//                intent.putExtra("Admins", "Admins");
//                startActivity(intent);
//            }
//        });

//        mBtnCheckNewOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CategoryActivity.this, AdminNewOrdersActivity.class);
//                startActivity(intent);
//            }
//        });

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        mImgScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, AddProductActivity.class);
                intent.putExtra("category", "science");
                startActivity(intent);
            }
        });

        mImgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, AddProductActivity.class);
                intent.putExtra("category", "text");
                startActivity(intent);
            }
        });

        mImgChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, AddProductActivity.class);
                intent.putExtra("category", "children");
                startActivity(intent);
            }
        });

        mImgForeignLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, AddProductActivity.class);
                intent.putExtra("category", "foreign");
                startActivity(intent);
            }
        });
    }
}
