package com.universityhillsocial.universityhillsocial;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePasswordActivity extends AppCompatActivity {


    private EditText newPassword;
    private Button updateButton;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPassword = findViewById(R.id.newPassword);
        updateButton = findViewById(R.id.updatePasswordButton);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //String password = newPassword.getText().toString().trim();
        //updateButton.setEnabled(false);

        //if(!password.equals("") && password.length() > 6) {
        //    updateButton.setEnabled(true);
        //}


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senderPassword = newPassword.getText().toString().trim();
                firebaseUser.updatePassword(senderPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdatePasswordActivity.this, "Password updated!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(UpdatePasswordActivity.this, "Password update failed. Problem occurred.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
