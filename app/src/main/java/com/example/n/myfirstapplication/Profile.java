package com.example.n.myfirstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView name;
    private TextView email;
    private TextView phone;
    //private TextView location = (TextView) findViewById(R.id.textLocation); //maybe


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").
                child(mAuth.getCurrentUser().getUid());

        name = (TextView) findViewById(R.id.textName);
        email = (TextView) findViewById(R.id.textEmail);
        phone = (TextView) findViewById(R.id.textPhone);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                printUserInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void printUserInfo(DataSnapshot dataSnapshot){
        User userInfo = dataSnapshot.getValue(User.class);
        name.setText(userInfo.getName());
        email.setText(userInfo.getEmail());
        phone.setText(userInfo.getPhone());
    }
}
