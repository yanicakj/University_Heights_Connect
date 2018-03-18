package com.universityhillsocial.universityhillsocial;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Context mContext = HomeActivity.this;
    private FirebaseAuth firebaseAuth;
    //private Toolbar toolbar
    //private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting");
        firebaseAuth = FirebaseAuth.getInstance();
        setViews();
        setupBottomNavigationView();

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
            case R.id.profileMenu: {
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
