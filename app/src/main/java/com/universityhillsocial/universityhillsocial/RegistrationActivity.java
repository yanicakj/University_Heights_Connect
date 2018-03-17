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


public class RegistrationActivity extends AppCompatActivity {

    private EditText registerEmail, registerPassword, registerUserName;
    private Button registerButton;
    private TextView loginJumpBack;
    private FirebaseAuth firebaseAuth;

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
        registerUserName = (EditText) findViewById(R.id.registerFirstName);

    }

    private Boolean validate() {

        Boolean filled = false;
        String inputEmail = registerEmail.getText().toString().trim();
        String inputPassword = registerPassword.getText().toString().trim();
        String inputUserName = registerUserName.getText().toString().trim();

        if (inputEmail.isEmpty() || inputPassword.isEmpty() || inputUserName.isEmpty())
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
}
