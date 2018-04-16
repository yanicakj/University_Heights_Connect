package com.universityhillsocial.universityhillsocial.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.universityhillsocial.universityhillsocial.R;

public class AddClassActivity extends AppCompatActivity {

    private EditText className, professorName;
    private Spinner semesterSpinner;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView backArrow;
    private Button addClassButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setViews();
        setOnClickListeners();

    }

    private void setViews() {
        className = findViewById(R.id.addClassClassName);
        professorName = findViewById(R.id.addClassProfessor);
        semesterSpinner = findViewById(R.id.addClassSemester);
        addClassButton = findViewById(R.id.addClassButton);
        backArrow = findViewById(R.id.addClassBackArrow);

    }

    private void validate() {

        String classNameString, professorNameString, semesterSpinnerString;
        classNameString = className.getText().toString().trim();
        professorNameString = professorName.getText().toString().trim();
        semesterSpinnerString = semesterSpinner.getSelectedItem().toString();
        final String id;


        if (!TextUtils.isEmpty(classNameString) && !TextUtils.isEmpty(professorNameString)) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            id = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("classes").push().getKey();
            DatabaseReference classRef = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("classes").child(id);

            classRef.child("classname").setValue(classNameString);
            classRef.child("professorname").setValue(professorNameString);
            classRef.child("semester").setValue(semesterSpinnerString);
            Toast.makeText(this, "Your info has been posted!", Toast.LENGTH_LONG).show();
            className.setText("");
            professorName.setText("");

        }
        else {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_LONG).show();
        }

    }

    private void setOnClickListeners() {

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
