package com.universityhillsocial.universityhillsocial;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText updateFirstName, updateLastName, updateEmail;
    private Button updateProfileButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                updateFirstName.setText(userProfile.getUserFirstName());
                updateLastName.setText(userProfile.getUserLastName());
                updateEmail.setText(userProfile.getUserEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, "Changes were not saved. Problem occurred.", Toast.LENGTH_LONG).show();
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = updateFirstName.getText().toString().trim();
                String lastName = updateLastName.getText().toString().trim();
                String email = updateEmail.getText().toString().trim();

                UserProfile userProfile = new UserProfile(email, firstName, lastName);
                databaseReference.setValue(userProfile);
                finish();
            }
        });

    }


    private void setViews() {
        updateFirstName = findViewById(R.id.updateFirstName);
        updateLastName = findViewById(R.id.updateLastName);
        updateEmail = findViewById(R.id.updateEmail);
        updateProfileButton = findViewById(R.id.sendProfileChanges);
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
