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
import com.universityhillsocial.universityhillsocial.R;

import java.util.ArrayList;

public class RemoveClassActivity extends AppCompatActivity {

    private ListView classListView2;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView backarrow;
    private static final String TAG = "RemoveClassActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_class);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setViews();
        populateFBData();
        setToolbarClickListeners();

    }

    private void setViews() {
        backarrow = findViewById(R.id.removeclassbackarrow);
    }

    private void populateFBData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser;
        firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference classRef = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("classes");
        classListView2 = findViewById(R.id.removeClassListView);

        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProfileClassItem> classitems = new ArrayList<ProfileClassItem>();
                for (DataSnapshot classitem : dataSnapshot.getChildren()) {
                    classitems.add(classitem.getValue(ProfileClassItem.class));
                }
                ClassAdapter2 newadapter = new ClassAdapter2(RemoveClassActivity.this, classitems);
                classListView2.setAdapter(newadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class ClassAdapter2 extends BaseAdapter {


        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView classname, professorname, semester;
        private ArrayList<ProfileClassItem> classList;


        public ClassAdapter2(Context context, ArrayList<ProfileClassItem> classes)
        {
            mContext = context;
            classList = classes;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return classList.size();
        }

        @Override
        public Object getItem(int position) {
            return classList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.profile_activity_single_class_item, null);
            }
            classname = convertView.findViewById(R.id.classNameTextView);
            classname.setText(classList.get(position).getClassname());
            professorname = convertView.findViewById(R.id.classProfessorTextView);
            professorname.setText(classList.get(position).getProfessorname());
            semester = convertView.findViewById(R.id.classSemesterTextView);
            semester.setText(classList.get(position).getSemester());

            return convertView;
        }
    }

    private void setToolbarClickListeners() {
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        classListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView classtv = view.findViewById(R.id.classNameTextView);
                final String classname = classtv.getText().toString();
                //Toast.makeText(RemoveClassActivity.this, "Removing " + classname, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(RemoveClassActivity.this);
                builder.setMessage("Are you sure you want to delete \n\n" + classname)
                        .setTitle("Class Delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("classes");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singleclass : dataSnapshot.getChildren()) {
                                    Log.d(TAG, "Entering inner datasnapshot");
                                    if (singleclass.child("classname").getValue().equals(classname)) {
                                        final String stringclass = singleclass.child("classname").getValue().toString();
                                        databaseReference.child(singleclass.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                Toast.makeText(RemoveClassActivity.this, stringclass + " has been removed from your profile", Toast.LENGTH_SHORT).show();
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
}





















