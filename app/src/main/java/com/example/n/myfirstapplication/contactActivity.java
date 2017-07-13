package com.example.n.myfirstapplication;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class contactActivity extends AppCompatActivity {

    private FloatingActionButton addContact;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mContacts;

    private HashSet<String> contactList = new HashSet<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mContacts = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("contacts");

        initListView();

        mContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // update list view
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    contactList.add(ds.getValue().toString());
                }
                adapter.clear();
                adapter.addAll(new ArrayList<>(contactList));
                adapter.notifyDataSetChanged();
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

    public void initListView(){
        adapter = new ArrayAdapter<>(
                this,
                R.layout.item_in_list_simple,
                new ArrayList<>(contactList)
        );

        ListView list = (ListView) findViewById(R.id.contactList);
        list.setAdapter(adapter);
    }

}
