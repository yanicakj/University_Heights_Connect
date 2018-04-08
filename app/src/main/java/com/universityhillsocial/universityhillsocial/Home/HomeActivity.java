package com.universityhillsocial.universityhillsocial.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.ui.database.FirebaseListAdapter;
//import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.universityhillsocial.universityhillsocial.DeadlineActivity;
import com.universityhillsocial.universityhillsocial.MainActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.SettingsActivity;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;
import com.universityhillsocial.universityhillsocial.utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Context mContext = HomeActivity.this;
    private FirebaseAuth firebaseAuth;
    private static final int ACITVITY_NUM = 0;
    private Toolbar toolbar;
    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private ProgressBar mProgressBar;
    private TextWatcher mSearchTw;
    private EditText searchHomeTop;
    private FirebaseUser firebaseUser;
    private ImageView deadlinesIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting");
        initImageLoader(); // needed right now for profile activity error
        //setupViewPager();
        firebaseAuth = FirebaseAuth.getInstance();
        setViews();
        setupBottomNavigationView();
        populateListView();
        setupTextWatcher();
        searchHomeTop.addTextChangedListener(mSearchTw);
        checkUserInFB();

        deadlinesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DeadlineActivity.class));
            }
        });

    }


    private void setViews() {
        mProgressBar = findViewById(R.id.homeProgressBar);
        mProgressBar.setVisibility(View.GONE);
        listView = findViewById(R.id.homeListView);
        searchHomeTop = findViewById(R.id.topHomeSearch);
        deadlinesIcon = findViewById(R.id.topHomeDeadlines);

    }

    // TODO : Change to RecyclerView
    private void populateListView() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventRef = firebaseDatabase.getReference("content");

        listView = findViewById(R.id.homeListView);

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HomeListViewItem> eventsInfo = new ArrayList<HomeListViewItem>();
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    eventsInfo.add(event.getValue(HomeListViewItem.class));
                    //eventNames.add(event.child("name").getValue().toString());

                }
                Collections.reverse(eventsInfo);
                SimpleAdapter newAdapter = new SimpleAdapter(HomeActivity.this, eventsInfo);
                //newAdapter = new EventAdapter(HomeActivity.this, eventsInfo);
                //ArrayAdapter<HomeListViewItem> arrayAdapter = new ArrayAdapter(HomeActivity.this, R.layout.home_activity_single_item, R.id.nameHomeTextView, eventsInfo);
                //ArrayAdapter<HomeListViewItem> arrayAdapter = new ArrayAdapter(HomeActivity.this, R.layout.home_activity_single_item, R.id.nameHomeTextView, eventNames);
                listView.setAdapter(newAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "An Error Has Occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupTextWatcher() {
        mSearchTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(HomeActivity.this, "Changed value of search", Toast.LENGTH_SHORT).show();
                // TODO : add filtering below
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
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

//    // Responsible for adding the 3 tabs on top bar: camera, home, and messages
//    private void setupViewPager() {
//        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        //adapter.addFragment(new CameraFragment());
//        adapter.addFragment(new HomeFragment());
//        adapter.addFragment(new MessagesFragment());
//        ViewPager viewPager = findViewById(R.id.container);
//        viewPager.setAdapter(adapter);
//        TabLayout tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_earth); // index 1
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_arrow); // index 2
//    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    public class SimpleAdapter extends BaseAdapter {


        private Context homeContext;
        private LayoutInflater layoutInflater;
        private TextView name, description, location, school;
        private ArrayList<HomeListViewItem> eventList;
        private ImageView imageView;


        public SimpleAdapter(Context context, ArrayList<HomeListViewItem> events)
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
                imageView.setImageResource(R.drawable.rutgerslogo);
            }

            return convertView;
        }
    }

    private void checkUserInFB() {

        firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference userReference = firebaseDatabase.getReference("users");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean newuser = true;
                for (DataSnapshot singleuser : dataSnapshot.getChildren()) {
                    if (singleuser.getKey().equals(firebaseUser.getUid())) {
                        newuser = false;
                    }
                }
                if (newuser) {
                    addUserToFBDB();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addUserToFBDB() {
        DatabaseReference userReference = firebaseDatabase.getReference("users");
        userReference.child(firebaseUser.getUid()).child("firstname").setValue("Update Your");
        userReference.child(firebaseUser.getUid()).child("lastname").setValue("Profile Info!");
        userReference.child(firebaseUser.getUid()).child("major").setValue("Major Not Declared Yet!");
        userReference.child(firebaseUser.getUid()).child("email").setValue("Add your email!");

    }

}
