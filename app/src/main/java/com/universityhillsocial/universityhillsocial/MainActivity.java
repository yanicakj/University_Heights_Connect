package com.universityhillsocial.universityhillsocial;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.app.Activity;
import android.view.Menu;
import android.app.IntentService;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private Button LoginButton;
    private TextView signUp;
    private FirebaseAuth firebaseAuth;
    private int counter = 5;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            //finish();
            //startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            checkEmailVerification();
        }

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    validateAuth(userEmail.getText().toString(), userPassword.getText().toString());
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

    }

    private void setViews() {

        userEmail = (EditText) findViewById(R.id.userEmail);
        userPassword = (EditText) findViewById(R.id.userPassword);
        LoginButton = (Button) findViewById(R.id.LogInButton);
        signUp = (TextView) findViewById(R.id.SignUp);

    }

    private Boolean validate() {

        Boolean filled = false;
        String inputEmail = userEmail.getText().toString();
        String inputPassword = userPassword.getText().toString();

        if (inputEmail.isEmpty() || inputPassword.isEmpty())
            Toast.makeText(this, "Please fill out all of the fields", Toast.LENGTH_SHORT).show();
        else
            filled = true;

        return filled;
    }

    private void validateAuth(String userName, String password) {

        progressDialog.setMessage("Checking!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    checkEmailVerification();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    counter--;
                    if (counter == 0) {
                        LoginButton.setEnabled(false);
                    }
                }
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        boolean emailFlag = firebaseUser.isEmailVerified();
        if (emailFlag) {
            //Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        }
        else {
            Toast.makeText(this, "Please Verify Your Email", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }
}

