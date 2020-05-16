package com.qat.android.quanlynhasach.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qat.android.quanlynhasach.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    private String mCategoryName, mDescription, mPrice, mProductName, mSaveCurrentDate, mSaveCurrentTime;
    private MyLoadingButton mBtnAddProduct;
    private ImageView mImgProduct;
    private EditText mEditTextProductName, mEditTextProductPrice, mEditTextProductDescription;
    private static final int mGalleryPick = 1;
    private Uri mImgUri;
    private String mProductRandomKey, mDownloadImgUrl;
    private StorageReference mProductImgRef;
    private DatabaseReference mProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        mCategoryName = getIntent().getExtras().get("category").toString();
        mProductImgRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        mProductRef = FirebaseDatabase.getInstance().getReference().child("Products");


        mBtnAddProduct = findViewById(R.id.btn_add_product);
        mImgProduct = findViewById(R.id.img_camera);

        mEditTextProductName = findViewById(R.id.edit_product_name);
        mEditTextProductPrice = findViewById(R.id.edit_product_price);
        mEditTextProductDescription = findViewById(R.id.edit_product_description);

        mImgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });


        mBtnAddProduct.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                ValidateProductData();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, mGalleryPick);
    }

    // Get Gallery from Storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == mGalleryPick && resultCode == RESULT_OK && data != null) {
            mImgUri = data.getData();
            mImgProduct.setImageURI(mImgUri);
        }
    }

    private void ValidateProductData() {
        mProductName = mEditTextProductName.getText().toString();
        mPrice = mEditTextProductPrice.getText().toString();
        mDescription = mEditTextProductDescription.getText().toString();

        if (mImgUri == null) {
            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();
            mBtnAddProduct.showNormalButton();
        } else if (TextUtils.isEmpty(mProductName) || mEditTextProductName.length() <= 5) {
            Toast.makeText(this, "Product name must not be less than 5 characters", Toast.LENGTH_SHORT).show();
            mBtnAddProduct.showNormalButton();
        } else if (TextUtils.isEmpty(mPrice) || mEditTextProductPrice.length() <= 3 || mEditTextProductPrice.length() >= 7) {
            Toast.makeText(this, "Product price must be 4 to 6 characters", Toast.LENGTH_SHORT).show();
            mBtnAddProduct.showNormalButton();
        } else if (TextUtils.isEmpty(mDescription) || mEditTextProductDescription.length() <= 10) {
            Toast.makeText(this, "Product description must not be less than 10 characters", Toast.LENGTH_SHORT).show();
            mBtnAddProduct.showNormalButton();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        mSaveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a");
        mSaveCurrentTime = currentTime.format(calendar.getTime());

        mProductRandomKey = mSaveCurrentDate + mSaveCurrentTime;


        final StorageReference filePath = mProductImgRef.child(mImgUri.getLastPathSegment() + mProductRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(mImgUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                mBtnAddProduct.showNormalButton();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProductActivity.this, "Product image uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        mDownloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            mDownloadImgUrl = task.getResult().toString();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", mProductRandomKey);
        productMap.put("date", mSaveCurrentDate);
        productMap.put("time", mSaveCurrentTime);
        productMap.put("description", mDescription);
        productMap.put("image", mDownloadImgUrl);
        productMap.put("category", mCategoryName);
        productMap.put("price", mPrice);
        productMap.put("pname", mProductName);

        mProductRef.child(mProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(AddProductActivity.this, CategoryActivity.class);
                    startActivity(intent);

                    mBtnAddProduct.showNormalButton();
                    Toast.makeText(AddProductActivity.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    mBtnAddProduct.showNormalButton();
                    String message = task.getException().toString();
                    Toast.makeText(AddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
