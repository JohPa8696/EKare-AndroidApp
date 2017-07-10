package com.example.n.myfirstapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class contactActivity extends AppCompatActivity {

    private FloatingActionButton addContact;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mContacts = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("contacts");

        mContacts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    addToScrollView(ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mContacts.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addContact = (FloatingActionButton) findViewById(R.id.addContactBtn);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showAddContactDialog();
            }
        });
    }

    public void showAddContactDialog(){
        DialogFragment dialog = new AddContactDialogFragment();
        dialog.show(getFragmentManager(), "AddContactDialog");
    }

    public void addToScrollView(String message){
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 0, 20); // llp.setMargins(left, top, right, bottom);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.contactList);

        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setBackgroundResource(R.drawable.message_background);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(20);
        textView.setPadding(35,10,25,10);
        textView.setLayoutParams(llp);

        linearLayout.addView(textView);
    }

}
