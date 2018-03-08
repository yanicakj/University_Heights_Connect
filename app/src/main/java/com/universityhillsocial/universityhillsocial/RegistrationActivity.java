package com.universityhillsocial.universityhillsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RegistrationActivity extends AppCompatActivity {

    private EditText registerEmail, registerPassword;
    private Button registerButton;
    private TextView loginJumpBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setViews();


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

    }


}
