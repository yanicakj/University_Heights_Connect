package com.universityhillsocial.universityhillsocial.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.universityhillsocial.universityhillsocial.DeadlineActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;
import com.universityhillsocial.universityhillsocial.utils.ListViewClassAdapter;
import com.universityhillsocial.universityhillsocial.utils.UniversalImageLoader;

import java.util.ArrayList;

/**
 * Created by Kubie on 3/18/18.
 */

public class ProfileActivity extends AppCompatActivity {


    private static final String TAG = "ProfileActivity";
    private Context mContext = ProfileActivity.this;
    private static final int ACITVITY_NUM = 2; //4;
    private ProgressBar mProgressBar;
    private ImageView profilePhoto;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ListView classListView;
    private int classCount;
    private TextView tvclassCount, displayNameProfile, majorProfile, emailProfile, editProfileSquare;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "OnCreate: Started");

        setViews();
        setupToolBar();
        setupBottomNavigationView();
        setProfileImage();

        firebaseAuth = FirebaseAuth.getInstance();

        populateFBdata();

        editProfileSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

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
                for (DataSnapshot classitem : dataSnapshot.getChildren()) {
                    classitems.add(classitem.getValue(ProfileClassItem.class));
                    classCount++;
                }
                // TODO : add last row option "Add a class"
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
                //String emailFB = dataSnapshot.child("website").getValue().toString();
                String emailFB = firebaseUser.getEmail();

                displayNameProfile.setText(firstnameFB + " " + lastnameFB);
                majorProfile.setText(majorFB);
                emailProfile.setText(emailFB);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setProfileImage() {
        String imgURL = "https://www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "");

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

}