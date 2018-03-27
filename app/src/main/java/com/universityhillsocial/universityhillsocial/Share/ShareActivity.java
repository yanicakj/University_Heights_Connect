package com.universityhillsocial.universityhillsocial.Share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.universityhillsocial.universityhillsocial.Home.HomeActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Kubie on 3/18/18.
 */

public class ShareActivity extends AppCompatActivity {


    private static final String TAG = "ShareActivity";
    private Context mContext = ShareActivity.this;
    private static final int ACITVITY_NUM = 1;
    private ImageView topBarIcon;
    //2;

    private EditText contentName, contentDescription, contentLocation;
    private Spinner schools;
    private Button addContentButton;
    private DatabaseReference classReference;
    private FirebaseDatabase firebaseDatabase;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_share);
        Log.d(TAG, "OnCreate: started");

        setupBottomNavigationView();
        setViews();
        topBarIcon = findViewById(R.id.topShareBarMenu);
        classReference = FirebaseDatabase.getInstance().getReference("classes");

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

    }


    private void addContentToDB() {
        String id;
        String name = contentName.getText().toString().trim();
        String description = contentDescription.getText().toString().trim();
        String location = contentLocation.getText().toString().trim();
        String school = schools.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) &&  !TextUtils.isEmpty(location)) {

            firebaseDatabase = FirebaseDatabase.getInstance();

            int randomNum = ThreadLocalRandom.current().nextInt(1, 10000000 + 1);
            id = "post" + String.valueOf(randomNum);
            DatabaseReference contentRef = firebaseDatabase.getReference("content").child(id);
            //contentRef.push().setValue(id);
            contentRef.child("name").setValue(name);
            contentRef.child("description").setValue(description);
            contentRef.child("location").setValue(location);
            contentRef.child("school").setValue(school);
            Toast.makeText(this, "Data added to DB", Toast.LENGTH_LONG).show();

//            String id = classReference.push().getKey();
//            ClassHolder classholder = new ClassHolder(contentname, professorname, credithour, semester);
//            classReference.child(id).setValue(classholder);
//            Toast.makeText(this, "Data added to DB", Toast.LENGTH_LONG).show();
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
}
