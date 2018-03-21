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

/**
 * Created by Kubie on 3/18/18.
 */

public class ShareActivity extends AppCompatActivity {


    private static final String TAG = "ShareActivity";
    private Context mContext = ShareActivity.this;
    private static final int ACITVITY_NUM = 1;
    private ImageView topBarIcon;
    //2;

    private EditText className, professorName, credits;
    private Spinner semesters;
    private Button addClassButton;
    private DatabaseReference classReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_share);
        Log.d(TAG, "OnCreate: started");

        setupBottomNavigationView();
        setViews();
        topBarIcon = findViewById(R.id.topShareBarMenu);
        classReference = FirebaseDatabase.getInstance().getReference("classes");

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClassToDB();
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


    private void addClassToDB() {
        String classname = className.getText().toString().trim();
        String professorname = professorName.getText().toString().trim();
        String credithour = credits.getText().toString().trim();
        String semester = semesters.getSelectedItem().toString();

        if(!TextUtils.isEmpty(classname) && !TextUtils.isEmpty(professorname) &&  !TextUtils.isEmpty(credithour)) {

            String id = classReference.push().getKey();
            ClassHolder classholder = new ClassHolder(classname, professorname, credithour, semester);
            classReference.child(id).setValue(classholder);
            Toast.makeText(this, "Data added to DB", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Please fill out all of the fields!", Toast.LENGTH_LONG).show();
        }
    }

    private void setViews() {
        className = findViewById(R.id.className);
        professorName = findViewById(R.id.professorName);
        credits = findViewById(R.id.credits);
        semesters = (Spinner) findViewById(R.id.semester);
        addClassButton = findViewById(R.id.addClassButton);

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
