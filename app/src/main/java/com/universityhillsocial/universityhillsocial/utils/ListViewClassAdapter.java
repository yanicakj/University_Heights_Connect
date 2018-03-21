package com.universityhillsocial.universityhillsocial.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.universityhillsocial.universityhillsocial.R;

import java.util.ArrayList;

/**
 * Created by Kubie on 3/19/18.
 */

public class ListViewClassAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private ArrayList<String> classes;

    public ListViewClassAdapter(Context context, int layoutResource, ArrayList<String> classes) {
        super(context ,layoutResource, classes);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.layoutResource = layoutResource;
        this.classes = classes;
    }

    private static class ViewHolder {
        TextView profileClass;
        ProgressBar mProgressBar;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mProgressBar = convertView.findViewById(R.id.listTextProgressBar);
            viewHolder.profileClass = convertView.findViewById(R.id.listTextView);
            viewHolder.mProgressBar.setVisibility(View.GONE);
            viewHolder.profileClass.setText(classes.get(position));

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
}
