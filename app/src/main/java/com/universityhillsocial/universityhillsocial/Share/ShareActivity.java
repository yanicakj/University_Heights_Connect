package com.universityhillsocial.universityhillsocial.Share;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.universityhillsocial.universityhillsocial.Home.HomeActivity;
import com.universityhillsocial.universityhillsocial.Profile.EditProfileActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Kubie on 3/18/18.
 */

public class ShareActivity extends AppCompatActivity {


    private static final String TAG = "ShareActivity";
    private Context mContext = ShareActivity.this;
    private static final int ACITVITY_NUM = 1;
    private ImageView topBarIcon, shareImageSquare;
    private EditText contentName, contentDescription, contentLocation;
    private Spinner schools;
    private Button addContentButton;
    //private DatabaseReference classReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    private Uri imageHoldUri = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_share);
        Log.d(TAG, "OnCreate: started");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setupBottomNavigationView();
        setViews();
        topBarIcon = findViewById(R.id.topShareBarMenu);
        //classReference = FirebaseDatabase.getInstance().getReference("classes");

        addContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContentToDB();
            }
        });


        topBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ShareActivity.this, HomeActivity.class));
            }
        });

        shareImageSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoto();
            }
        });

    }


    private void addContentToDB() {
        final String id;
        String name = contentName.getText().toString().trim();
        String description = contentDescription.getText().toString().trim();
        String location = contentLocation.getText().toString().trim();
        String school = schools.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) &&  !TextUtils.isEmpty(location)) {

            firebaseDatabase = FirebaseDatabase.getInstance();
            id = firebaseDatabase.getReference("content").push().getKey();

            FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference shareref = storageRef.child(id + "/" + imageHoldUri.getLastPathSegment());
                UploadTask uploadTask = shareref.putFile(imageHoldUri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ShareActivity.this, "Upload Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        DatabaseReference contentRef2 = firebaseDatabase.getReference("content").child(id);
                        contentRef2.child("image").setValue(downloadUrl.toString());
                    }
                });


            DatabaseReference contentRef = firebaseDatabase.getReference("content").child(id);

            contentRef.child("name").setValue(name);
            contentRef.child("description").setValue(description);
            contentRef.child("location").setValue(location);
            contentRef.child("school").setValue(school);
            contentRef.child("poster").setValue(firebaseUser.getUid());
            Toast.makeText(this, "Your info has been posted!", Toast.LENGTH_LONG).show();
            contentName.setText("");
            contentDescription.setText("");
            contentLocation.setText("");
            shareImageSquare.setImageResource(R.drawable.ic_camera2);

        }
        else {
            Toast.makeText(this, "Please fill out all of the fields!", Toast.LENGTH_LONG).show();
        }
    }

    private void setViews() {
        contentName = findViewById(R.id.contentName);
        contentDescription = findViewById(R.id.contentDescription);
        contentLocation = findViewById(R.id.contentLocation);
        schools = (Spinner) findViewById(R.id.school);
        addContentButton = findViewById(R.id.addContentButton);
        shareImageSquare = findViewById(R.id.shareImageView);

    }


    private void setupBottomNavigationView() {
        Log.d(TAG, "Setting up Bottom Navigation View");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACITVITY_NUM);
        menuItem.setChecked(true);
    }

    private void changePhoto() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
        builder.setTitle("Add a Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {

        // Take photo with camera
        Log.d("SharePicture", "started camera here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        // Choose image from gallery
        Log.d("SharePicture", "started gallery here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SharePicture", "started activity result");

        // Save image URI from gallery
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            imageHoldUri = imageUri;

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(15,10)
                    .start(this);

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            // Save image URI from camera
            Uri imageUri = data.getData();
            imageHoldUri = imageUri;

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(15,10)
                    .start(this);

        }

        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();
                shareImageSquare.setImageURI(imageHoldUri);

//                FirebaseStorage storage = FirebaseStorage.getInstance();
//                StorageReference storageRef = storage.getReference();
//                StorageReference editprofileref = storageRef.child(firebaseAuth.getUid() + "/" + imageHoldUri.getLastPathSegment());
//                UploadTask uploadTask = editprofileref.putFile(imageHoldUri);
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Toast.makeText(ShareActivity.this, "Upload Unsuccessful", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        if (downloadUrl != null) {
//                            //Log.d("DownloadURI", downloadUrl.toString());
//                            DatabaseReference uriRef = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
//                            uriRef.child("profilepic").setValue(downloadUrl.toString());
//                        }
//                        Toast.makeText(ShareActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }

    }

}
