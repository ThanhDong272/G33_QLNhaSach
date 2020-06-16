package com.qat.android.quanlynhasach.user_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.qat.android.quanlynhasach.CheckConnection;
import com.qat.android.quanlynhasach.R;
import com.qat.android.quanlynhasach.constants.Constants;
import com.qat.android.quanlynhasach.user.MainActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private CircleImageView mImgProfile;
    private EditText mEditTextFullName, mEditTextPhoneNumber,
            mEditTextAddress, mEditTextEmail;
    private Spinner mSpinnerSex;
    private MyLoadingButton mBtnUpdateProfile;

    private Uri mImgUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSpinnerSex = getActivity().findViewById(R.id.spinner_sex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_item_sex, R.layout.spinner_item_sex);
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown_sex);
        mSpinnerSex.setAdapter(adapter);

        //Action Bar
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Profile");

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        mImgProfile = getActivity().findViewById(R.id.img_profile);
        mEditTextFullName = getActivity().findViewById(R.id.edit_full_name);
        mEditTextPhoneNumber = getActivity().findViewById(R.id.edit_phone_number);
        mEditTextAddress = getActivity().findViewById(R.id.edit_address);
        mEditTextEmail = getActivity().findViewById(R.id.edit_email);

        mImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                Intent intent = CropImage.activity()
                        .setAspectRatio(1, 1)
                        .getIntent(getActivity());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        mBtnUpdateProfile = getActivity().findViewById(R.id.btn_update_profile);

        userInfoDisplay(mImgProfile, mEditTextFullName, mSpinnerSex, mEditTextPhoneNumber, mEditTextAddress, mEditTextEmail);

        mBtnUpdateProfile.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
            @Override
            public void onMyLoadingButtonClick() {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });
    }

    private void userInfoDisplay(final CircleImageView mImgProfile, final EditText mEditTextFullName, final Spinner mSpinnerSex,
                                 final EditText mEditTextPhoneNumber, final EditText mEditTextAddress, final EditText mEditTextEmail) {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Constants.currentOnlineUser.getUsername());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String fullName = dataSnapshot.child("fullName").getValue().toString();
                        String sex = dataSnapshot.child("sex").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        Picasso.get().load(image).into(mImgProfile);
                        mEditTextFullName.setText(fullName);
                        mSpinnerSex.setPrompt(sex);
                        mEditTextPhoneNumber.setText(phone);
                        mEditTextAddress.setText(address);
                        mEditTextEmail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", mEditTextFullName.getText().toString());
        userMap.put("sex", mSpinnerSex.getSelectedItem().toString());
        userMap.put("phone", mEditTextPhoneNumber.getText().toString());
        userMap.put("address", mEditTextAddress.getText().toString());
        userMap.put("email", mEditTextEmail.getText().toString());

        ref.child(Constants.currentOnlineUser.getUsername()).updateChildren(userMap);

        startActivity(new Intent(getContext(), MainActivity.class));
        Toast.makeText(getContext(), "Profile update successfully", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImgUri = result.getUri();
            mImgProfile.setImageURI(mImgUri);
        }
    }

    private void userInfoSaved() {
        String fullName = mEditTextFullName.getText().toString();
        String sex = mSpinnerSex.getSelectedItem().toString();
        String phoneNumber = mEditTextPhoneNumber.getText().toString();
        String address = mEditTextAddress.getText().toString();
        String email = mEditTextEmail.getText().toString();

        if (TextUtils.isEmpty(fullName) || mEditTextFullName.length() <= 5 || mEditTextFullName.length() >= 51) {
            Toast.makeText(getContext(), "Full name must be 6 to 50 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (TextUtils.isEmpty(sex)) {
            Toast.makeText(getContext(), "Sex is mandatory", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (TextUtils.isEmpty(phoneNumber) || mEditTextPhoneNumber.length() <= 9 || mEditTextPhoneNumber.length() >= 13) {
            Toast.makeText(getContext(), "Phone number must be 10 to 12 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (TextUtils.isEmpty(address) || mEditTextAddress.length() <= 9 || mEditTextAddress.length() >= 101) {
            Toast.makeText(getContext(), "Address must be 10 to 100 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (TextUtils.isEmpty(email) || mEditTextEmail.length() <= 9 || mEditTextEmail.length() >= 51) {
            Toast.makeText(getContext(), "Email must be 10 to 50 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (checker.equals("clicked") && isValidEmail(email)) {
            uploadImage();
        } else {
            Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (mImgUri != null) {
            final StorageReference fileRef = storageProfilePrictureRef.child(Constants.currentOnlineUser.getUsername() + ".jpg");

            uploadTask = fileRef.putFile(mImgUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        if (CheckConnection.isOnline(getContext())) {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("fullName", mEditTextFullName.getText().toString());
                            userMap.put("sex", mSpinnerSex.getSelectedItem().toString());
                            userMap.put("phone", mEditTextPhoneNumber.getText().toString());
                            userMap.put("address", mEditTextAddress.getText().toString());
                            userMap.put("email", mEditTextEmail.getText().toString());
                            userMap.put("image", myUrl);
                            ref.child(Constants.currentOnlineUser.getUsername()).updateChildren(userMap);

                            progressDialog.dismiss();

                            startActivity(new Intent(getContext(), MainActivity.class));
                            Toast.makeText(getContext(), "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Image is not selected.", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
