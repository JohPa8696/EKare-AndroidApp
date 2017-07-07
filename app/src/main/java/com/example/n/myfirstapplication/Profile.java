package com.example.n.myfirstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView name = (TextView) findViewById(R.id.textName);
    private TextView email = (TextView) findViewById(R.id.textEmail);
    private TextView phone = (TextView) findViewById(R.id.textPhone);
    private TextView location = (TextView) findViewById(R.id.textLocation); //maybe


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void printUserInfo(DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            User userInfo = ds.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
            name.setText(userInfo.getName());
            email.setText(userInfo.getEmail());
            phone.setText(userInfo.getPhone());
        }
    }
}
