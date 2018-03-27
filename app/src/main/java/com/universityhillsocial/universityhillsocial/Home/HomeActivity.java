package com.universityhillsocial.universityhillsocial.Home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.universityhillsocial.universityhillsocial.MainActivity;
import com.universityhillsocial.universityhillsocial.OldProfileActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.SettingsActivity;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;
import com.universityhillsocial.universityhillsocial.utils.SectionsPagerAdapter;
import com.universityhillsocial.universityhillsocial.utils.UniversalImageLoader;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Context mContext = HomeActivity.this;
    private FirebaseAuth firebaseAuth;
    private static final int ACITVITY_NUM = 0;
    //private Toolbar toolbar
    //private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting");
        initImageLoader(); // needed right now for profile activity error

        firebaseAuth = FirebaseAuth.getInstance();
        setViews();
        setupBottomNavigationView();








        //setupViewPager();
        //initToolbar();
    }

    private void setViews() {
        //toolbar = findViewById(R.id.homeToolbar);
        //listView = findViewById(R.id.listViewHome);

    }

//    private void initToolbar() {
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Put Search Here");
//
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

    // Responsible for adding the 3 tabs on top bar: camera, home, and messages
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(new CameraFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MessagesFragment());
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_earth); // index 1
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_arrow); // index 2
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

//    public class SimpleAdapter extends BaseAdapter {
//
//
//        private Context homeContext;
//        private LayoutInflater layoutInflater;
//        private TextView title, description;
//        private String[] titleArray, descriptionArray;
//        private ImageView imageView;
//
//
//        public SimpleAdapter(Context context, String[] title, String[] description) {
//            homeContext = context;
//            titleArray = title;
//            descriptionArray = description;
//            layoutInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            return titleArray.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return titleArray[position];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = layoutInflater.inflate(R.layout.home_activity_single_item, null);
//            }
//            title = convertView.findViewById(R.id.textViewHome);
//            description = convertView.findViewById(R.id.homeDescription);
//            imageView = convertView.findViewById(R.id.imageViewHome);
//            title.setText(titleArray[position]);
//            description.setText(descriptionArray[position]);
//            //return
//        }
//    }


}
