package com.universityhillsocial.universityhillsocial.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.storage.FirebaseStorage;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.universityhillsocial.universityhillsocial.DeadlineActivity;
import com.universityhillsocial.universityhillsocial.R;
import com.universityhillsocial.universityhillsocial.utils.BottomNavigationViewHelper;
import com.universityhillsocial.universityhillsocial.utils.UniversalImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
    private ImageView deadlinesIcon, messagesIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting");

        initImageLoader(); // needed right now for profile activity error
        firebaseAuth = FirebaseAuth.getInstance();
        setViews();
        setupBottomNavigationView();
        populateListView();
        //setupTextWatcher();
        //searchHomeTop.addTextChangedListener(mSearchTw);
        checkUserInFB();

        setOnClickListeners();
    }


    private void setViews() {
        mProgressBar = findViewById(R.id.homeProgressBar);
        mProgressBar.setVisibility(View.GONE);
        listView = findViewById(R.id.homeListView);
        //searchHomeTop = findViewById(R.id.topHomeSearch);
        deadlinesIcon = findViewById(R.id.topHomeDeadlines);
        messagesIcon = findViewById(R.id.topHomeMessage);

    }

    // TODO : Change to RecyclerView ...maybe
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
                }
                Collections.reverse(eventsInfo);
                SimpleAdapter newAdapter = new SimpleAdapter(HomeActivity.this, eventsInfo);
                listView.setAdapter(newAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "An Error Has Occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void setupTextWatcher() {
//        mSearchTw = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(HomeActivity.this, "Changed value of search", Toast.LENGTH_SHORT).show();
//                // TODO : add filtering below
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        };
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


    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    public class SimpleAdapter extends BaseAdapter {


        private Context homeContext;
        private LayoutInflater layoutInflater;
        private TextView name, description, location, school;
        private ArrayList<HomeListViewItem> eventList;
        private ImageView imageView, homePoster;


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
            homePoster = convertView.findViewById(R.id.homePosterImage);
            name.setText(eventList.get(position).getName());
            description.setText(eventList.get(position).getDescription());
            location.setText(eventList.get(position).getLocation());
            school.setText(eventList.get(position).getSchool());

            String poster = eventList.get(position).getPoster();
            final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(poster);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        if (item.getKey().equals("profilepic")) {
                            String imgURL = item.getValue().toString();
                            UniversalImageLoader.setImage(imgURL, homePoster, mProgressBar, "");
                            if (!imgURL.equals("")) {
                                // UniversalImageLoader.setImage(imgURL, homePoster, mProgressBar, "");
                                Uri imageUri = Uri.parse(imgURL);
                                Bitmap bitmap2;
                                try {
                                    bitmap2 = getThumbnail(imageUri);
                                    homePoster.setImageBitmap(bitmap2);
                                    //deadlinesIcon.setImageBitmap(bitmap2);
                                } catch (IOException e) {
                                    // do nothing
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // do nothing
                }
            });




            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            String imageStringFB = "";
            imageStringFB = eventList.get(position).getImage();
            //UniversalImageLoader.setImage(imageStringFB, imageView, mProgressBar, "");
            String imageFile = "";
            imageFile = eventList.get(position).getImage();

            //Log.d("Adapter uri", imageFile);
            if (!imageFile.equals("")) {
                UniversalImageLoader.setImage(imageStringFB, imageView, mProgressBar, "");
                Uri imageUri = Uri.parse(imageFile);
                Bitmap bitmap;

                try {
                    bitmap = getThumbnail(imageUri);
                    imageView.setImageBitmap(bitmap);
                    //deadlinesIcon.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // do nothing
                }
            }
            else {
                if (school.getText().toString().trim().equals("Essex County College")) {
                    imageView.setImageResource(R.drawable.ecclogo);
                } else if (school.getText().toString().trim().equals("New Jersey Institute of Technology")) {
                    imageView.setImageResource(R.drawable.njitlogo);
                } else {
                    imageView.setImageResource(R.drawable.rutgerspictwo);
                }

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
        userReference.child(firebaseUser.getUid()).child("school").setValue("none");
        userReference.child(firebaseUser.getUid()).child("profilepic").setValue("https://www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf");
        userReference.child(firebaseUser.getUid()).child("email").setValue(firebaseUser.getEmail());


    }

    private void setOnClickListeners() {

        final String[] messages = {
                "Have a great day!",
                "Don't forget to smile!",
                "You got this!",
                "Never quit!",
                "Don't give up!",
                "You're almost there!",
                "Get that degree!",
                "You miss 99% of the shots you don't take!",
                "The best preparation for tomorrow is doing your best today!",
                "Believe. Achieve. Succeed!"
                };

        deadlinesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DeadlineActivity.class));
            }
        });

        messagesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random ran = new Random();
                int spot = ran.nextInt(10);
                String message = messages[spot];

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage(message);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {


        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 480) ? (originalSize / 480) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

}
