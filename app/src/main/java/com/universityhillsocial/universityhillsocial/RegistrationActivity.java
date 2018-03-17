package com.universityhillsocial.universityhillsocial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private EditText registerEmail, registerPassword, registerFirstName, registerLastName;
    private Button registerButton;
    private TextView loginJumpBack;
    private FirebaseAuth firebaseAuth;
    String inputEmail, inputPassword, inputFirstName, inputLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        setViews();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    String user_email = registerEmail.getText().toString().trim();
                    String user_password = registerPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();
                            } else {
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

    private void setViews() {

        registerButton = (Button) findViewById(R.id.RegisterButton);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        loginJumpBack = (TextView) findViewById(R.id.LoginJumpBack);
        registerFirstName = (EditText) findViewById(R.id.registerFirstName);
        registerLastName = (EditText) findViewById(R.id.registerLastName);

    }

    private Boolean validate() {

        Boolean filled = false;
        inputEmail = registerEmail.getText().toString().trim();
        inputPassword = registerPassword.getText().toString().trim();
        inputFirstName = registerFirstName.getText().toString().trim();
        inputLastName = registerLastName.getText().toString().trim();


        if (inputEmail.isEmpty() || inputPassword.isEmpty() || inputFirstName.isEmpty() || inputLastName.isEmpty())
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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(inputEmail, inputFirstName, inputLastName);
        databaseReference.setValue(userProfile);
    }

}
