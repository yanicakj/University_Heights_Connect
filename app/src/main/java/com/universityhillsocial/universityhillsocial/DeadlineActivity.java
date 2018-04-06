package com.universityhillsocial.universityhillsocial;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DeadlineActivity extends AppCompatActivity {

    private ImageView deadlineIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline);

        setViews();

        deadlineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setViews() {

        deadlineIcon = findViewById(R.id.deadlinesbackarrow);

    }
}
