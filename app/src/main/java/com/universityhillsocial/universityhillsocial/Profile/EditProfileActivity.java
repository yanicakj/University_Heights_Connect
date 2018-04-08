package com.universityhillsocial.universityhillsocial.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.universityhillsocial.universityhillsocial.R;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etfirstname, etlastname, etmajor, etphone;
    private Button removeClassButton, removePostButton;
    private ImageView backButton, pushToFirebaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setViews();
        setClickListeners();

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
    }

    private void setClickListeners() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

}
