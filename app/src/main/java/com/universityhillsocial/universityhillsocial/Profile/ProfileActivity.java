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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
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

        populateClasses();
        //tempListSetup();

    }

    private void tempListSetup() {
        ArrayList<String> classes = new ArrayList<String>();
        classes.add("CSC228");
        classes.add("CSC231");
        classes.add("CSC235");
        classes.add("CSC237");
        classes.add("MTH136");
        classes.add("MTH221");
        classes.add("MTH122");
        classes.add("MTH121");
        classes.add("CSC112");
        classes.add("HST101");

        setupProfileListView(classes);
    }

    private void populateClasses() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference classRef = firebaseDatabase.getReference("users").child("id").child("classes");

        classListView = findViewById(R.id.listViewProfile);

        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProfileClassItem> classitems = new ArrayList<ProfileClassItem>();
                for (DataSnapshot classitem : dataSnapshot.getChildren()) {
                    classitems.add(classitem.getValue(ProfileClassItem.class));
                }
                ClassAdapter newadapter = new ClassAdapter(ProfileActivity.this, classitems);
                classListView.setAdapter(newadapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupProfileListView(ArrayList<String> classes) {
        ListView listView = findViewById(R.id.listViewProfile);

        ListViewClassAdapter adapter = new ListViewClassAdapter(mContext, R.layout.layout_list_classview, classes);
        listView.setAdapter(adapter);
    }


    private void setProfileImage() {
        String imgURL = "https://www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "");

    }

    private void setViews() {
        mProgressBar = findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = findViewById(R.id.profile_photo);

    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = findViewById(R.id.topProfileBarMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Navigating to Account Settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
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