package com.universityhillsocial.universityhillsocial.Home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universityhillsocial.universityhillsocial.R;

/**
 * Created by Kubie on 3/18/18.
 */

public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        return view;
    }


}
