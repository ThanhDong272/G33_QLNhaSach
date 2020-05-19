package com.qat.android.quanlynhasach.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.models.Books;
import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    private TextView mTxtBookName, mTxtBookPrice, mTxtBookCategory, mTxtBookAuthor, mTxtBookDescription;
    private Button mBtnAddToCart, mBtnAddToBorrow;
    private ElegantNumberButton mNumberButton;
    private ImageView mImgBook;
    private String productID = "", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        productID = getIntent().getStringExtra("pid");

        mTxtBookName = findViewById(R.id.txt_book_detail_name);
        mTxtBookPrice = findViewById(R.id.txt_book_detail_price);
        mTxtBookCategory = findViewById(R.id.txt_book_detail_category);
        mTxtBookAuthor = findViewById(R.id.txt_book_detail_author);
        mTxtBookDescription = findViewById(R.id.txt_book_detail_description);

        mImgBook = findViewById(R.id.img_book_detail);

        mBtnAddToCart = findViewById(R.id.btn_add_to_cart);
        mBtnAddToBorrow = findViewById(R.id.btn_add_to_borrow);

        mNumberButton = findViewById(R.id.btn_number);

        getBookDetails(productID);
    }

    private void getBookDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Books books = dataSnapshot.getValue(Books.class);

                    mTxtBookName.setText(books.getPname());
                    mTxtBookPrice.setText(books.getPrice() + " ₫");
                    mTxtBookCategory.setText(books.getCategory());
                    mTxtBookAuthor.setText(books.getAuthor());
                    mTxtBookDescription.setText(books.getDescription());
                    Picasso.get().load(books.getImage()).into(mImgBook);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
