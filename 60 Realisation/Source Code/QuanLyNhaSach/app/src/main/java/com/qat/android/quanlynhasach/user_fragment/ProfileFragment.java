package com.qat.android.quanlynhasach.user_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText mEditTextFullName, mEditTextSex,
            mEditTextPhoneNumber, mEditTextAddress, mEditTextEmail;
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

        //Action Bar
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Profile");

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        mImgProfile = getActivity().findViewById(R.id.img_profile);
        mEditTextFullName = getActivity().findViewById(R.id.edit_full_name);
        mEditTextSex = getActivity().findViewById(R.id.edit_sex);
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
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
//                getActivity().getFragmentManager().popBackStack();
//                CropImage.activity(imageUri)
//                        .setAspectRatio(1, 1)
//                        .start((Activity) getContext());
            }
        });

        mBtnUpdateProfile = getActivity().findViewById(R.id.btn_update_profile);

        userInfoDisplay(mImgProfile, mEditTextFullName, mEditTextSex,
                mEditTextPhoneNumber, mEditTextAddress, mEditTextEmail);

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

    private void userInfoDisplay(final CircleImageView mImgProfile, final EditText mEditTextFullName, final EditText mEditTextSex,
                                 final EditText mEditTextPhoneNumber, final EditText mEditTextAddress, final EditText mEditTextEmail) {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Constants.currentOnlineUser.getUsername());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String fullName = dataSnapshot.child("full_name").getValue().toString();
                        String sex = dataSnapshot.child("sex").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        Picasso.get().load(image).into(mImgProfile);
                        mEditTextFullName.setText(fullName);
                        mEditTextSex.setText(sex);
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
        userMap.put("full_name", mEditTextFullName.getText().toString());
        userMap.put("sex", mEditTextSex.getText().toString());
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
        String sex = mEditTextSex.getText().toString();
        String phoneNumber = mEditTextPhoneNumber.getText().toString();
        String address = mEditTextAddress.getText().toString();
        String email = mEditTextEmail.getText().toString();

        if (TextUtils.isEmpty(fullName) || mEditTextFullName.length() <= 5 || mEditTextFullName.length() >= 51) {
            Toast.makeText(getContext(), "Full name must be 6 to 50 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (TextUtils.isEmpty(sex) || mEditTextSex.length() <= 1 || mEditTextSex.length() >= 4) {
            Toast.makeText(getContext(), "Sex must be 2 or 3 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (TextUtils.isEmpty(phoneNumber) || mEditTextPhoneNumber.length() <= 6 || mEditTextPhoneNumber.length() >= 12) {
            Toast.makeText(getContext(), "Phone number must be 7 to 11 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (TextUtils.isEmpty(address) || mEditTextAddress.length() <= 9 || mEditTextAddress.length() >= 101) {
            Toast.makeText(getContext(), "Address must be 10 to 100 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (TextUtils.isEmpty(email) || mEditTextEmail.length() <= 9 || mEditTextEmail.length() >= 51) {
            Toast.makeText(getContext(), "Email must be 10 to 50 characters", Toast.LENGTH_SHORT).show();
            mBtnUpdateProfile.showNormalButton();
        } else if (checker.equals("clicked")) {
            uploadImage();
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
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("full_name", mEditTextFullName.getText().toString());
                        userMap.put("sex", mEditTextSex.getText().toString());
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
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }
}
