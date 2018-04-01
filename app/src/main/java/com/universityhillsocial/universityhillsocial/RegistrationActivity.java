package com.universityhillsocial.universityhillsocial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity {

    private EditText registerEmail, registerPassword, registerFirstName, registerLastName,
        registerClass, registerProfessor, registerSemester;
    private Button registerButton;
    private TextView loginJumpBack;
    private FirebaseAuth firebaseAuth;
    String inputEmail, inputPassword, inputFirstName, inputLastName, inputClass, inputProfessor, inputSemester;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setViews();

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    final String user_email = registerEmail.getText().toString().trim();
                    String user_password = registerPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();
                            }
                            else {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed. Please try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        loginJumpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });



    }

    private void addUserToDB() {

//        firebaseDatabase = FirebaseDatabase.getInstance();
//
//        String user_email = registerEmail.getText().toString().trim();
//        String userId = user_email.substring(0, user_email.indexOf("@"));
//        DatabaseReference registerRef = firebaseDatabase.getReference("users").child(userId);
//
//        registerRef.child("firstname").setValue(registerFirstName.getText().toString().trim());
//        registerRef.child("lastname").setValue(registerLastName.getText().toString().trim());
//        registerRef.child("email").setValue(registerEmail.getText().toString().trim());

    }

    private void setViews() {

        registerButton = findViewById(R.id.RegisterButton);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        loginJumpBack = findViewById(R.id.LoginJumpBack);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);
        registerClass = findViewById(R.id.registerClass);
        registerProfessor = findViewById(R.id.registerProfessorName);
        registerSemester = findViewById(R.id.registerSemester);

    }

    private Boolean validate() {

        Boolean filled = false;
        inputEmail = registerEmail.getText().toString().trim();
        inputPassword = registerPassword.getText().toString().trim();
        inputFirstName = registerFirstName.getText().toString().trim();
        inputLastName = registerLastName.getText().toString().trim();
        inputClass = registerClass.getText().toString().trim();
        inputProfessor = registerProfessor.getText().toString().trim();
        inputSemester = registerSemester.getText().toString().trim();


        if (inputEmail.isEmpty() || inputPassword.isEmpty()
                || inputFirstName.isEmpty() || inputLastName.isEmpty()
                || inputClass.isEmpty() || inputProfessor.isEmpty()
                || inputSemester.isEmpty())
            Toast.makeText(this, "Please fill out all of the fields!", Toast.LENGTH_LONG).show();
        else
            filled = true;

        return filled;
    }

    private void sendEmailVerification() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(RegistrationActivity.this, "Email Verification Sent", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this, "Verification Email Failed to Send", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void sendUserData() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String inputEmailId = inputEmail.substring(0, inputEmail.indexOf("@"));
        Log.d("inputEmail as Key", "inputEmail is " + inputEmail);
        DatabaseReference userRef = firebaseDatabase.getReference("users").child(user.getUid());

        userRef.child("email").setValue(inputEmail);
        userRef.child("firstname").setValue(inputFirstName);
        userRef.child("lastname").setValue(inputLastName);
        String newref = userRef.child("classes").push().getKey();
        userRef.child("classes").child(newref).child("classname").setValue(inputClass);
        userRef.child("classes").child(newref).child("professorname").setValue(inputProfessor);
        userRef.child("classes").child(newref).child("semester").setValue(inputSemester);

        // previous test:
        //DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        //UserProfile userProfile = new UserProfile(inputEmail, inputFirstName, inputLastName);
        //databaseReference.setValue(userProfile);
    }

}
