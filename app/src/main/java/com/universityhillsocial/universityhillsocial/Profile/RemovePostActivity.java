package com.universityhillsocial.universityhillsocial.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.universityhillsocial.universityhillsocial.Home.HomeActivity;
import com.universityhillsocial.universityhillsocial.Home.HomeListViewItem;
import com.universityhillsocial.universityhillsocial.R;

import java.util.ArrayList;
import java.util.Collections;

public class RemovePostActivity extends AppCompatActivity {

    private ListView postListView;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView backarrow;
    private static final String TAG = "RemovePostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_post);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setViews();
        populateFBData();
        setToolbarClickListeners();

    }

    private void setViews() {
        backarrow = findViewById(R.id.removepostbackarrow);
    }


    private void populateFBData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventRef = firebaseDatabase.getReference("content");

        postListView = findViewById(R.id.removePostListView);

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HomeListViewItem> eventsInfo = new ArrayList<HomeListViewItem>();
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    if (event.child("poster").getValue().equals(firebaseUser.getUid())) {
                        eventsInfo.add(event.getValue(HomeListViewItem.class));
                    }
                }
                Collections.reverse(eventsInfo);
                SimpleAdapter2 newAdapter = new SimpleAdapter2(RemovePostActivity.this, eventsInfo);
                postListView.setAdapter(newAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RemovePostActivity.this, "An Error Has Occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setToolbarClickListeners() {

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView posttv = view.findViewById(R.id.nameHomeTextView);
                final String postname = posttv.getText().toString();
                //Toast.makeText(RemoveClassActivity.this, "Removing " + classname, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(RemovePostActivity.this);
                builder.setMessage("Are you sure you want to delete \n\n" + postname)
                        .setTitle("Post Delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        final DatabaseReference databaseReference = firebaseDatabase.getReference("content");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singlepost : dataSnapshot.getChildren()) {
                                    Log.d(TAG, "Entering inner datasnapshot");
                                    if (singlepost.child("name").getValue().equals(postname)) {
                                        final String stringpost = singlepost.child("name").getValue().toString();
                                        databaseReference.child(singlepost.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                Toast.makeText(RemovePostActivity.this, stringpost + " has been removed from public posts", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

    }

    private class SimpleAdapter2 extends BaseAdapter {


        private Context homeContext;
        private LayoutInflater layoutInflater;
        private TextView name, description, location, school;
        private ArrayList<HomeListViewItem> eventList;
        private ImageView imageView;


        public SimpleAdapter2(Context context, ArrayList<HomeListViewItem> events)
        {
            homeContext = context;
            eventList = events;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return eventList.size();
        }

        @Override
        public Object getItem(int position) {
            return eventList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.home_activity_single_item, null);
            }
            name = convertView.findViewById(R.id.nameHomeTextView);
            description = convertView.findViewById(R.id.homeDescriptionTextView);
            location = convertView.findViewById(R.id.locationHomeTextView);
            school = convertView.findViewById(R.id.schoolHomeTextView);
            imageView = convertView.findViewById(R.id.imageViewHome);
            name.setText(eventList.get(position).getName());
            description.setText(eventList.get(position).getDescription());
            location.setText(eventList.get(position).getLocation());
            school.setText(eventList.get(position).getSchool());

            if (school.getText().toString().trim().equals("Essex County College")) {
                imageView.setImageResource(R.drawable.ecclogo);
            } else if (school.getText().toString().trim().equals("New Jersey Institute of Technology")) {
                imageView.setImageResource(R.drawable.njitlogo);
            } else {
                imageView.setImageResource(R.drawable.rutgerspictwo);
            }

            return convertView;
        }
    }

}
