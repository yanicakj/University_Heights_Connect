package com.universityhillsocial.universityhillsocial.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.universityhillsocial.universityhillsocial.MainActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;
import com.universityhillsocial.universityhillsocial.utils.SectionsStatePageAdapter;

import java.util.ArrayList;

/**
 * Created by Kubie on 3/18/18.
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccSettings Activity";
    private Context mContext;
    private SectionsStatePageAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private static final int ACITVITY_NUM = 2;//4;

    //just for demo
    private FirebaseAuth firebaseAuth;
    private TextView editProfile, signOut;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Log.d(TAG, "OnCreate: Starting");
        mContext = AccountSettingsActivity.this;
        mViewPager = findViewById(R.id.container);
        mRelativeLayout = findViewById(R.id.relLayout1);

        setupBottomNavigationView();
        //setupSettingsList();
        //setupFragments();

        //fore demo
        firebaseAuth = FirebaseAuth.getInstance();

        // Setting up back arrow
        ImageView backArrow = findViewById(R.id.backarrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Navigating back to profile activity");
                finish();

                // for testing
                //firebaseAuth.signOut();
                //startActivity(new Intent(AccountSettingsActivity.this, MainActivity.class));
            }
        });

        editProfile = findViewById(R.id.editProfile);
        signOut = findViewById(R.id.signOut);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettingsActivity.this, EditProfileActivity.class));
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                firebaseAuth.signOut();
                startActivity(new Intent(AccountSettingsActivity.this, MainActivity.class));
            }
        });



    }

//    private void setupSettingsList() {
//        Log.d(TAG, "Initializing Account Settings Listview");
//        ListView listView = findViewById(R.id.lvAccountSettings);
//
//        ArrayList<String> options = new ArrayList<>();
//        options.add(getString(R.string.edit_profile_fragment)); // fragment 0
//        options.add(getString(R.string.sign_out_fragment)); // fragment 1
//
//        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "Navigating to position " + position);
//                setmViewPager(position);
//            }
//        });
//
//    }
//
//    private void setmViewPager(int fragmentNumber) {
//        mRelativeLayout.setVisibility(View.GONE);
//        Log.d(TAG, "Navigating to frag " + fragmentNumber);
//        mViewPager.setAdapter(pagerAdapter);
//        mViewPager.setCurrentItem(fragmentNumber);
//    }
//
//    private void setupFragments() {
//        pagerAdapter = new SectionsStatePageAdapter(getSupportFragmentManager());
//        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile_fragment)); // fragment 0
//        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.sign_out_fragment)); // fragment 1
//
//    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "Setting up Bottom Navigation View");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACITVITY_NUM);
        menuItem.setChecked(true);
    }

}
