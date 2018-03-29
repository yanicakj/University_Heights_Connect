package com.universityhillsocial.universityhillsocial.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.firebase.ui.database.FirebaseListAdapter;
//import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.universityhillsocial.universityhillsocial.MainActivity;
import com.universityhillsocial.universityhillsocial.OldProfileActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.SettingsActivity;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;
import com.universityhillsocial.universityhillsocial.utils.SectionsPagerAdapter;
import com.universityhillsocial.universityhillsocial.utils.UniversalImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Context mContext = HomeActivity.this;
    private FirebaseAuth firebaseAuth;
    private static final int ACITVITY_NUM = 0;
    private Toolbar toolbar;
    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    //private EventAdapter newAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting");

        initImageLoader(); // needed right now for profile activity error
        //initToolbar();
        //setupViewPager();
        firebaseAuth = FirebaseAuth.getInstance();
        setViews();
        setupBottomNavigationView();
        populateListView();



    }

    private void setViews() {
        //toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.homeListView);

    }

//    private void initToolbar() {
//        setSupportActionBar(R.id.);
//        getSupportActionBar().setTitle("Put Search Here");
//
//    }


    private void populateListView() {


        final ArrayList<String> eventNames = new ArrayList<String>();
        final ArrayList<String> eventDescriptions = new ArrayList<String>();
        final ArrayList<String> eventLocations = new ArrayList<String>();
        final ArrayList<String> eventSchools = new ArrayList<String>();

        //ArrayList<HomeListViewItem> eventsInfo = new ArrayList<HomeListViewItem>();
        //EventAdapter adapter = new EventAdapter(this, eventsInfo);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventRef = firebaseDatabase.getReference("content");

        //final ListView listView = findViewById(R.id.homeListView);
        listView = findViewById(R.id.homeListView);

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HomeListViewItem> eventsInfo = new ArrayList<HomeListViewItem>();
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    eventsInfo.add(event.getValue(HomeListViewItem.class));
                    //eventNames.add(event.child("name").getValue().toString());

                }
                SimpleAdapter newAdapter = new SimpleAdapter(HomeActivity.this, eventsInfo);
                //newAdapter = new EventAdapter(HomeActivity.this, eventsInfo);
                //ArrayAdapter<HomeListViewItem> arrayAdapter = new ArrayAdapter(HomeActivity.this, R.layout.home_activity_single_item, R.id.nameHomeTextView, eventsInfo);
                //ArrayAdapter<HomeListViewItem> arrayAdapter = new ArrayAdapter(HomeActivity.this, R.layout.home_activity_single_item, R.id.nameHomeTextView, eventNames);
                listView.setAdapter(newAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//    public class EventAdapter extends ArrayAdapter<HomeListViewItem> {
//
//        private Context mContext;
//        private List<HomeListViewItem> mEvents = new ArrayList<>();
//
//        public EventAdapter(Context context, ArrayList<HomeListViewItem> events) {
//            super(context, 0, events);
//            mContext = context;
//            mEvents = events;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//            View listItem = convertView;
//
//            //HomeListViewItem event = getItem(position);
//
//            if (convertView == null) {
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.home_activity_single_item, parent, false);
//            }
//
//            HomeListViewItem currentItem = mEvents.get(position);
//
//            TextView eventName = listItem.findViewById(R.id.nameHomeTextView);
//            eventName.setText(currentItem.getName());
//            TextView eventDescription = listItem.findViewById(R.id.homeDescriptionTextView);
//            eventDescription.setText(currentItem.getDescription());
//            TextView eventLocation = listItem.findViewById(R.id.locationHomeTextView);
//            eventLocation.setText(currentItem.getLocation());
//            TextView eventSchool = listItem.findViewById(R.id.schoolHomeTextView);
//            eventSchool.setText(currentItem.getSchool());
//
//            //eventName.setText(event.name);
//            //eventDescription.setText(event.description);
//            //eventLocation.setText(event.location);
//            //eventSchool.setText(event.school);
//
//            return convertView;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.logoutMenu: {
                logout();
                break;
            }
            case R.id.settingsMenu: {
                settings();
                break;
            }
            case R.id.oldprofileMenu: {
                profile();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    private void settings() {
        startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
    }

    private void profile() {
        finish();
        startActivity(new Intent(HomeActivity.this, OldProfileActivity.class));
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
        //private String[] titleArray, descriptionArray;
        private ArrayList<HomeListViewItem> eventList;
        private ImageView imageView;


        public SimpleAdapter(Context context, ArrayList<HomeListViewItem> events)  //String[] title, String[] description
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
            return convertView;
        }
    }


}
