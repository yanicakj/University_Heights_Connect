package com.universityhillsocial.universityhillsocial.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.universityhillsocial.universityhillsocial.R;

/**
 * Created by Kubie on 3/18/18.
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "Account Settings Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Log.d(TAG, "OnCreate: Starting");

        


    }
}
