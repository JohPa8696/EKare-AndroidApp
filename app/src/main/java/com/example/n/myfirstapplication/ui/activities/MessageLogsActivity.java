package com.example.n.myfirstapplication.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.n.myfirstapplication.dto.Contact;
import com.example.n.myfirstapplication.dto.MessageLog;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.ui.adapter.MessageLogListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class MessageLogsActivity extends AppCompatActivity {
    private ListView messageLogsLv;
    private MessageLogListAdapter adapter;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mContactsRef;
    private FirebaseAuth userAuth;

    private List<MessageLog> messageLogList;
    private Intent messagesScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_logs);

        messagesScreen = new Intent(this, MessageScreenActivity.class);
        messageLogsLv = (ListView) findViewById(R.id.messagelogs_listview);
        messageLogList= new ArrayList<>();
        //TODO get the list of contacts from database
        userAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
        mContactsRef= mDatabaseRef.child("users").child(userAuth.getCurrentUser().getUid()).child("contacts");

        mContactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                messageLogList.clear();
                for(DataSnapshot child: children) {
                    String messageLogId = child.getKey();
                    Contact contact = child.getValue(Contact.class);
                    // Add an empty message log object to the hash map

                    messageLogList.add(new MessageLog(child.getKey(), contact.getName(),contact.getLastMessage(),contact.getDate()));

                }
                adapter = new MessageLogListAdapter(getApplicationContext(), messageLogList);
                messageLogsLv.setAdapter(adapter);

                messageLogsLv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id",view.getTag().toString());
                        bundle.putString("title",((MessageLog)adapter.getItem(i)).getUserName());
                        messagesScreen.putExtras(bundle);
                        startActivity(messagesScreen);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
