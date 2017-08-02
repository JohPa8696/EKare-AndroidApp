package com.example.n.myfirstapplication.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.n.myfirstapplication.dto.Contact;
import com.example.n.myfirstapplication.dto.MessageLog;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.ui.adapters.MessageLogListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageLogsActivity extends AppCompatActivity {
    private ListView messageLogsLv;
    private MessageLogListAdapter adapter;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mContactsRef;
    private DatabaseReference mUsersRef;
    private FirebaseAuth userAuth;
    private TextView numConversations;
    private HashMap<String,MessageLog> messageLogList;
    private Intent messagesScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_logs);
        setTitle("Conversations");
        messagesScreen = new Intent(this, MessageScreenActivity.class);
        messageLogsLv = (ListView) findViewById(R.id.messagelogs_listview);
        messageLogList= new HashMap<>();

        numConversations = (TextView) findViewById(R.id.noConvo_tv);
        // Get users contact from databse and store locally in a hashmap
        userAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
        mContactsRef= mDatabaseRef.child("users").child(userAuth.getCurrentUser().getUid()).child("contacts");

        // Getting the contact list
        mContactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                messageLogList.clear();

                // Each datasnapshot is a contact instance
                // We populate the contact list and pass it to the adapter which does the UI stuff
                for(DataSnapshot child: children) {
                    String messageLogId = child.getKey();
                    Contact contact = child.getValue(Contact.class);
                    messageLogList.put(contact.getEmail(),
                            new MessageLog(messageLogId, contact.getName(),contact.getLastMessage(),contact.getDate()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get contact profile Uri
        mUsersRef = mDatabaseRef.child("users");
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    User user = child.getValue(User.class);
                    if(messageLogList.get(user.getEmail().trim())!= null){
                        messageLogList.get(user.getEmail().trim()).setProfileUri(user.getProfilePicUri());
                    }
                }

                // Pass the list of message_receiver.xml log to the adapter
                adapter = new MessageLogListAdapter(getApplicationContext(), new ArrayList<MessageLog>(messageLogList.values()));
                messageLogsLv.setAdapter(adapter);

                //set text view
                if(messageLogList.keySet().size() >1){
                    numConversations.setText(messageLogList.keySet().size() +" conversations");
                }else{
                    numConversations.setText(messageLogList.keySet().size() +" conversation");
                }

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
//
//    /**
//     * THis function get the profile image Uri for each of the contacts and pass it to the adapter
//     */
//    private void initiateAdapter(){
//        DatabaseReference usersRef = mDatabaseRef.child("users");
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot child : dataSnapshot.getChildren()){
//                    User user = child.getValue(User.class);
//                    if(messageLogList.get(user.getEmail().trim())!= null){
//                        messageLogList.get(user.getEmail().trim()).setProfileUri(user.getProfilePicUri());
//                    }
//                }
//
//                // Pass the list of message_receiver.xml log to the adapter
//                adapter = new MessageLogListAdapter(getApplicationContext(), (ArrayList<MessageLog>) messageLogList.values());
//                messageLogsLv.setAdapter(adapter);
//
//                messageLogsLv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("id",view.getTag().toString());
//                        bundle.putString("title",((MessageLog)adapter.getItem(i)).getUserName());
//                        messagesScreen.putExtras(bundle);
//                        startActivity(messagesScreen);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
