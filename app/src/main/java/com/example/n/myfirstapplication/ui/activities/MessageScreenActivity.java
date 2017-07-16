package com.example.n.myfirstapplication.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.n.myfirstapplication.Message;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.ui.adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MessageScreenActivity extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mdbReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ListView messagesLv;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);

        // Extract message log id
        Bundle mlID = getIntent().getExtras();
        String messageLogId= mlID.get("id").toString();

        mdatabase = FirebaseDatabase.getInstance();
        mdbReference = mdatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference();

        messagesLv = (ListView) findViewById(R.id.messages_lv);
        messages = new ArrayList<>();

        // Get messages from database

        DatabaseReference messagesdbRef= mdbReference.child("message-log").child(messageLogId).child("messages");

        messagesdbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    System.out.println(child.getValue());
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                messageAdapter = new MessageAdapter(getApplicationContext(),messages);
                messagesLv.setAdapter(messageAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Get messages and display them

    }
}
