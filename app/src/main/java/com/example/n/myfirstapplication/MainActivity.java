package com.example.n.myfirstapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.ui.activities.MessageLogsActivity;
import com.example.n.myfirstapplication.ui.activities.MessageScreenActivity;
import com.example.n.myfirstapplication.ui.adapters.SectionsPageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity{


    private SectionsPageAdapter sectionAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mMesaages;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout layout = (TabLayout) findViewById(R.id.tabs);
        layout.setupWithViewPager(mViewPager);

//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mUser = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());
//        mMesaages = mUser.child("messagesReceived");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
////        mUser.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                printUserInfo(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        mMesaages.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MessageLogsActivity(),"Conversations");
       // adapter.addFragment(new ContactActivity(), "Contacts");
        viewPager.setAdapter(adapter);
    }
//    public void onButtonTap(View v){
//        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notify=new Notification.Builder
//                (getApplicationContext()).setContentTitle("Title").setContentText("Body").
//                setContentTitle("Subject").setSmallIcon(R.drawable.ic_launcher).build();
//
//        notify.flags |= Notification.FLAG_AUTO_CANCEL;
//        notif.notify(0, notify);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()){
//            case R.id.action_settings:
//                Intent profileActivity = new Intent(this, Profile.class);
//                startActivity(profileActivity);
//                return true;
//            case R.id.action_logout:
//                FirebaseAuth.getInstance().signOut();
//                logout();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            addToScrollView(intent.getStringExtra("message_receiver.xml"));
//        }
//    };
//
//    @Override
//    public void onResume(){
//        super.onResume();
//
//        // Register mMessageReceiver to receive messages.
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
//                new IntentFilter("my-event"));
//    }
//
//    @Override
//    public void onPause(){
//        // Unregister since the activity is not visible
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
//        super.onPause();
//    }
//
//    public void addToScrollView(String message){
//        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        llp.setMargins(0, 0, 0, 20); // llp.setMargins(left, top, right, bottom);
//
//        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.scrollLayout);
//
//        TextView textView = new TextView(this);
//        textView.setText(message);
//        textView.setBackgroundResource(R.drawable.message_background);
//        textView.setTextColor(Color.BLACK);
//        textView.setTextSize(20);
//        textView.setPadding(35,10,25,10);
//        textView.setLayoutParams(llp);
//
//        linearLayout.addView(textView);
//    }
//
//    private void printUserInfo(DataSnapshot dataSnapshot){
//        User userInfo = dataSnapshot.getValue(User.class);
//        String message = "Name: " + userInfo.getName() + "\n"
//                + "E-mail: " + userInfo.getEmail() + "\n"
//                + "Phone: " + userInfo.getPhone();
//        addToScrollView(message);
//
//        Log.d("DataBase", "reading from database");
//    }
//
//    private void logout(){
//        if(mAuth.getCurrentUser() == null){
//            Intent authActivity = new Intent(this, Authentication.class);
//            startActivity(authActivity);
//        }else{
//            Toast.makeText(MainActivity.this, "Logout failed.",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
}
