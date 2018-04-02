package com.universityhillsocial.universityhillsocial.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.universityhillsocial.universityhillsocial.Home.HomeActivity;
import com.universityhillsocial.universityhillsocial.Profile.ProfileActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.Share.ShareActivity;

/**
 * Created by Kubie on 3/18/18.
 */

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationHelper";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "Setting up Bottom Navigation View with Helper");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.ic_house: {
                        Intent intent1 = new Intent(context, HomeActivity.class);
                        context.startActivity(intent1);
                        break;
                    }
//                    case R.id.ic_search: {
//                        Intent intent2 = new Intent(context, SearchActivity.class);
//                        context.startActivity(intent2);
//                        break;
//                    }
                    case R.id.ic_circle: {
                        Intent intent3 = new Intent(context, ShareActivity.class);
                        context.startActivity(intent3);
                        break;
                    }
//                    case R.id.ic_alert: {
//                        Intent intent4 = new Intent(context, LikesActivity.class);
//                        context.startActivity(intent4);
//                        break;
//                    }
                    case R.id.ic_android: {
                        Intent intent5 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent5);
                        break;
                    }

                }

                return false;
            }
        });


    }


}
