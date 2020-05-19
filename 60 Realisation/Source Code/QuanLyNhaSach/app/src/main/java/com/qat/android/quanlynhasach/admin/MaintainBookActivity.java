package com.qat.android.quanlynhasach.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MaintainBookActivity extends AppCompatActivity {

    private MyLoadingButton mBtnApplyChanges, mBtnDelete;
    private EditText mEditTextBookName, mEditTextBookPrice, mEditTextBookQuantity, mEditTextBookAuthor, mEditTextBookDescription;
    private ImageView mImageView;

    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_book);

        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        mEditTextBookName = findViewById(R.id.edit_book_name);
        mEditTextBookPrice = findViewById(R.id.edit_book_price);
        mEditTextBookQuantity = findViewById(R.id.edit_book_quantity);
        mEditTextBookAuthor = findViewById(R.id.edit_book_author);
        mEditTextBookDescription = findViewById(R.id.edit_book_description);

        mImageView = findViewById(R.id.book_image_maintain);

        mBtnApplyChanges = findViewById(R.id.btn_apply_changes);
        mBtnDelete = findViewById(R.id.btn_delete);

        displaySpecificBookInfo();

        mBtnApplyChanges.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                applyChanges();
            }
        });

        mBtnDelete.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                deleteBook();
            }
        });
    }

    private void deleteBook() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(MaintainBookActivity.this, MainAdminActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(MaintainBookActivity.this, "The Book is deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String name = mEditTextBookName.getText().toString();
        String price = mEditTextBookPrice.getText().toString();
        String quantity = mEditTextBookQuantity.getText().toString();
        String author = mEditTextBookAuthor.getText().toString();
        String description = mEditTextBookDescription.getText().toString();

        if (name.equals("") || mEditTextBookName.length() <= 5) {
            Toast.makeText(this, "Book name must not be less than 5 characters", Toast.LENGTH_SHORT).show();
            mBtnApplyChanges.showNormalButton();
        } else if (price.equals("") || mEditTextBookPrice.length() <= 3 || mEditTextBookPrice.length() >= 7) {
            Toast.makeText(this, "Book price must be 4 to 6 characters", Toast.LENGTH_SHORT).show();
            mBtnApplyChanges.showNormalButton();
        } else if (quantity.equals("")) {
            Toast.makeText(this, "Book quantity must not be null", Toast.LENGTH_SHORT).show();
            mBtnApplyChanges.showNormalButton();
        } else if (author.equals("")) {
            Toast.makeText(this, "Author must not be null", Toast.LENGTH_SHORT).show();
            mBtnApplyChanges.showNormalButton();
        } else if (description.equals("") || mEditTextBookDescription.length() <= 10) {
            Toast.makeText(this, "Book description must not be less than 10 characters", Toast.LENGTH_SHORT).show();
            mBtnApplyChanges.showNormalButton();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("pname", name);
            productMap.put("price", price);
            productMap.put("quantity", quantity);
            productMap.put("author", author);
            productMap.put("description", description);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MaintainBookActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();
                        mBtnApplyChanges.showNormalButton();
                        Intent intent = new Intent(MaintainBookActivity.this, MainAdminActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void displaySpecificBookInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pQuantity = dataSnapshot.child("quantity").getValue().toString();
                    String pAuthor = dataSnapshot.child("author").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    mEditTextBookName.setText(pName);
                    mEditTextBookPrice.setText(pPrice);
                    mEditTextBookQuantity.setText(pQuantity);
                    mEditTextBookAuthor.setText(pAuthor);
                    mEditTextBookDescription.setText(pDescription);
                    Picasso.get().load(pImage).into(mImageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
