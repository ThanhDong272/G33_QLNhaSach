package com.qat.android.quanlynhasach.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.admin.AddBookActivity;
import com.qat.android.quanlynhasach.admin.MainAdminActivity;
import com.qat.android.quanlynhasach.constants.Constants;
import com.qat.android.quanlynhasach.models.Books;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BookDetailActivity extends AppCompatActivity {

    private TextView mTxtBookName, mTxtBookPrice, mTxtBookCategory,
            mTxtBookAuthor, mTxtBookReleaseDate, mTxtBookDescription;
    private Button mBtnAddToCart;
    private ElegantNumberButton mNumberButton;
    private ImageView mImgBook;
    private String productID = "", state = "Normal";

    private String mSaveCurrentDate, mSaveCurrentTime;
    private String mProductRandomKey, mDownloadImgUrl;
    private StorageReference mProductImgRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        productID = getIntent().getStringExtra("pid");

        mTxtBookName = findViewById(R.id.txt_book_detail_name);
        mTxtBookPrice = findViewById(R.id.txt_book_detail_price);
        mTxtBookCategory = findViewById(R.id.txt_book_detail_category);
        mTxtBookAuthor = findViewById(R.id.txt_book_detail_author);
        mTxtBookReleaseDate = findViewById(R.id.txt_book_detail_release_date);
        mTxtBookDescription = findViewById(R.id.txt_book_detail_description);

        mImgBook = findViewById(R.id.img_book_detail);

        mBtnAddToCart = findViewById(R.id.btn_add_to_cart);

        mNumberButton = findViewById(R.id.btn_number);

        getBookDetails(productID);

        mBtnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCartList();
            }
        });
    }

    private void addingToCartList() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        mSaveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a");
        mSaveCurrentTime = currentTime.format(calendar.getTime());


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", mTxtBookName.getText().toString());
        cartMap.put("price", mTxtBookPrice.getText().toString());
        cartMap.put("category", mTxtBookCategory.getText().toString());
        cartMap.put("author", mTxtBookAuthor.getText().toString());
        cartMap.put("release_date", mTxtBookReleaseDate.getText().toString());
        cartMap.put("description", mTxtBookDescription.getText().toString());
        cartMap.put("date", mSaveCurrentDate);
        cartMap.put("time", mSaveCurrentTime);
        cartMap.put("quantity", mNumberButton.getNumber());

        cartListRef.child("User View").child(Constants.currentOnlineUser.getUsername())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(Constants.currentOnlineUser.getUsername())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(BookDetailActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(BookDetailActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void getBookDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pCategory = dataSnapshot.child("category").getValue().toString();
                    String pAuthor = dataSnapshot.child("author").getValue().toString();
                    String pReleaseDate = dataSnapshot.child("release_date").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    mTxtBookName.setText(pName);
                    mTxtBookPrice.setText(pPrice);
                    mTxtBookCategory.setText(pCategory);
                    mTxtBookAuthor.setText(pAuthor);
                    mTxtBookReleaseDate.setText(pReleaseDate);
                    mTxtBookDescription.setText(pDescription);
                    Picasso.get().load(pImage).into(mImgBook);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
