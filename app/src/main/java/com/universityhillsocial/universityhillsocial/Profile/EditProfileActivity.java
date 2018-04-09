package com.universityhillsocial.universityhillsocial.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.Share.ShareActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etfirstname, etlastname, etmajor, etphone;
    private Button removeClassButton, removePostButton, addClassButton, addPostButton;
    private ImageView backButton, pushToFirebaseButton, userImageProfileView, checkmarkIcon;
    private TextView changeProfilePic;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    private Uri imageHoldUri = null;
    private String inputfirstname, inputlastname, inputmajor, inputphone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setViews();
        setClickListeners();

        DatabaseReference databaseReference = firebaseDatabase.getReference("users");


    }

    private void setViews() {
        etfirstname = findViewById(R.id.editProfileFirstName);
        etlastname = findViewById(R.id.editProfileLastName);
        etmajor = findViewById(R.id.editProfileMajor);
        etphone = findViewById(R.id.editProfilePhone);
        removeClassButton = findViewById(R.id.editProfileRemoveClassButton);
        removePostButton = findViewById(R.id.editProfileRemovePostButton);
        backButton = findViewById(R.id.editprofilebackarrow);
        pushToFirebaseButton = findViewById(R.id.checkmark);
        changeProfilePic = findViewById(R.id.changeProfilePhoto);
        userImageProfileView = findViewById(R.id.profile_photo);
        addClassButton = findViewById(R.id.editProfileAddClassButton);
        addPostButton = findViewById(R.id.editProfileAddPostButton);
        checkmarkIcon = findViewById(R.id.checkmark);

    }

    private void setClickListeners() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        removeClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, RemoveClassActivity.class));
            }
        });

        removePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, RemovePostActivity.class));
            }
        });

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoto();
            }
        });

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(EditProfileActivity.this, ShareActivity.class));
            }
        });

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, AddClassActivity.class));
            }
        });

        checkmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputfirstname = etfirstname.getText().toString().trim();
                inputlastname = etlastname.getText().toString().trim();
                inputmajor = etmajor.getText().toString().trim();
                inputphone = etphone.getText().toString().trim();

                DatabaseReference updateRef = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
                if (!inputfirstname.isEmpty()) {
                    updateRef.child("firstname").setValue(inputfirstname);
                }
                if (!inputlastname.isEmpty()) {
                    updateRef.child("lastname").setValue(inputlastname);
                }
                if (!inputmajor.isEmpty()) {
                    updateRef.child("major").setValue(inputmajor);
                }
                if (!inputphone.isEmpty()) {
                    // do nothing for now
                }
                finish();
                Toast.makeText(EditProfileActivity.this, "Your Information Has Been Updated!", Toast.LENGTH_LONG).show();
            }
        });

    }

    // method to add picture to FB
    private void changePhoto() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
        Log.d("EditProfilePicture", "started camera here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        // Choose image from gallery
        Log.d("EditProfilePicture", "started gallery here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Save image URI from gallery
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            // Save image URI from camera
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();
                userImageProfileView.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }

    }

}
