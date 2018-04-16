package com.universityhillsocial.universityhillsocial.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.universityhillsocial.universityhillsocial.DeadlineActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;
import com.universityhillsocial.universityhillsocial.utils.ListViewClassAdapter;
import com.universityhillsocial.universityhillsocial.utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Kubie on 3/18/18.
 */

public class ProfileActivity extends AppCompatActivity {


    private static final String TAG = "ProfileActivity";
    private Context mContext = ProfileActivity.this;
    private static final int ACITVITY_NUM = 2; //4;
    private ProgressBar mProgressBar;
    private ImageView profilePhoto, schoolIcon;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser firebaseUser;
    private ListView classListView;
    private int classCount;
    private TextView tvclassCount, displayNameProfile, majorProfile, emailProfile, editProfileSquare;
    private Uri profilePicUri = null;
    private String imgURL;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "OnCreate: Started");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setViews();
        setupToolBar();
        setupBottomNavigationView();
        populateFBdata();
        setProfileImage();
        setOnClickListeners();


    }


    private void populateFBdata() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser;
        firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference classRef = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("classes");
        final DatabaseReference userRef = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
        classListView = findViewById(R.id.listViewProfile);
        classCount = 0;

        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProfileClassItem> classitems = new ArrayList<ProfileClassItem>();
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot classitem : dataSnapshot.getChildren()) {
                        classitems.add(classitem.getValue(ProfileClassItem.class));
                        classCount++;
                    }
                    //Collections.reverse(classitems);
                }
                    // "Add a Class!" is used in if statement in clicklistener - check before editing
                    ProfileClassItem lastitem = new ProfileClassItem("Add a Class!", "Click Here To Start!", "Trust the process.");
                    classitems.add(lastitem);
                    ClassAdapter newadapter = new ClassAdapter(ProfileActivity.this, classitems);
                    classListView.setAdapter(newadapter);
                    tvclassCount.setText(String.valueOf(classCount));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstnameFB = dataSnapshot.child("firstname").getValue().toString();
                String lastnameFB = dataSnapshot.child("lastname").getValue().toString();
                String majorFB = dataSnapshot.child("major").getValue().toString();
                String emailFB = firebaseUser.getEmail();
                String schoolFB = dataSnapshot.child("school").getValue().toString();

                displayNameProfile.setText(firstnameFB + " " + lastnameFB);
                majorProfile.setText(majorFB);
                emailProfile.setText(emailFB);

                if (schoolFB.equals("Essex County College")) {
                    schoolIcon.setImageResource(R.drawable.ecclogo);
                }
                else if (schoolFB.equals("Rutgers University")) {
                    schoolIcon.setImageResource(R.drawable.rutgerspictwo);
                }
                else if (schoolFB.equals("New Jersey Institute of Technology")) {
                    schoolIcon.setImageResource(R.drawable.njitlogo);
                }
                else {
                    schoolIcon.setImageResource(R.drawable.ic_school2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void setProfileImage() {

        DatabaseReference profilepicref = firebaseDatabase.getReference("users").child(firebaseUser.getUid());
        profilepicref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    if (item.getKey().equals("profilepic")) {
                        //Toast.makeText(ProfileActivity.this, item.getValue().toString(), Toast.LENGTH_LONG).show();
                        imgURL = item.getValue().toString();
                        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                imgURL = "https://www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
                UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "");
            }
        });



        //StorageReference storageRef = firebaseStorage.getReference();
        //StorageReference profilePicRef = storageRef.child(firebaseAuth.getUid() + "/");

    }

    private void setViews() {
        mProgressBar = findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = findViewById(R.id.profile_photo);
        tvclassCount = findViewById(R.id.tvClasses2);
        displayNameProfile = findViewById(R.id.displayNameProfile);
        majorProfile = findViewById(R.id.majorProfile);
        emailProfile = findViewById(R.id.emailProfile);
        editProfileSquare = findViewById(R.id.textEditProfile);
        schoolIcon = findViewById(R.id.ivSchool2);
    }

    private void setupToolBar() {

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "Setting up toolbar");
        ImageView profileMenu = findViewById(R.id.topProfileBarMenu);
        ImageView deadlineIcon = findViewById(R.id.topProfileDeadlineIcon);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Navigating to Account Settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
        deadlineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Navigating to Deadlines Activity");
                startActivity(new Intent(ProfileActivity.this, DeadlineActivity.class));
            }
        });

    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "Setting up Bottom Navigation View");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACITVITY_NUM);
        menuItem.setChecked(true);
    }

    public class ClassAdapter extends BaseAdapter {


        private Context homeContext;
        private LayoutInflater layoutInflater;
        private TextView classname, professorname, semester;
        private ArrayList<ProfileClassItem> classList;


        public ClassAdapter(Context context, ArrayList<ProfileClassItem> classes)
        {
            homeContext = context;
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

    private void setOnClickListeners() {

        editProfileSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView classtv = view.findViewById(R.id.classNameTextView);
                String classitemname = classtv.getText().toString();
                Log.d(TAG, "Classname clicked is : " + classitemname);
                if (classitemname.equals("Add a Class!")) {
                    startActivity(new Intent(ProfileActivity.this, AddClassActivity.class));
                }

            }
        });
    }

}