package com.example.n.myfirstapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageLogsActivity extends AppCompatActivity {
    private ListView messageLogsLv;
    private MessageLogListAdapter adapter;
    private List<MessageLog> messageLogList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mContactsRef;
    private FirebaseAuth userAuth;

    private Intent messagesScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_logs);

        messagesScreen = new Intent(this, MainActivity.class);
        messageLogsLv = (ListView) findViewById(R.id.messagelogs_listview);
        messageLogList = new ArrayList<>();

        //TODO get the list of contacts from database
        userAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
        mContactsRef= mDatabaseRef.child("users").child(userAuth.getCurrentUser().getUid()).child("contacts");

        mContactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child: children){
                    String messageLogId = child.getKey();
                    //TODO: design database so that getting the username, lsat message and timestamp are easy.
                    String userName = child.getValue().toString();
                    String lastMessage ="Fall detected.Please Confirm";
                    String timestamp = "Jul 15";
                    messageLogList.clear();
                    messageLogList.add(new MessageLog(messageLogId,userName,lastMessage,timestamp));
                }

                adapter = new MessageLogListAdapter(getApplicationContext(), messageLogList);
                messageLogsLv.setAdapter(adapter);

                messageLogsLv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(),"CLICKED" + view.getTag(),Toast.LENGTH_SHORT).show();
                        startActivity(messagesScreen);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

//    /**
//     * get the latest message in the message log
//     * @param messageLogID
//     * @return
//     */
//    private Message getLatestMessage(String messageLogID){
//        final Message latestMessage;
//        Query latestMessageQuery=mDatabaseRef.child("message-log")
//                                             .child(messageLogID)
//                                             .child("messages")
//                                             .orderByKey().limitToLast(1);
//        latestMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot child : dataSnapshot.getChildren()){
//                    Message mes = child.getValue(Message.class);
//                    latestMessage=mes;
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}
