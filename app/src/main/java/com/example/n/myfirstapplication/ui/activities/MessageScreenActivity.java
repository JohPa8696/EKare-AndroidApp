package com.example.n.myfirstapplication.ui.activities;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.myfirstapplication.Manifest;
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

public class MessageScreenActivity extends AppCompatActivity{
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    private FirebaseDatabase mdatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mdbReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ListView messagesLv;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private String senderUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);

        // Extract message log id
        Bundle mlID = getIntent().getExtras();
        String messageLogId= mlID.get("id").toString();
        senderUsername = mlID.get("username").toString();
        mdatabase = FirebaseDatabase.getInstance();
        mdbReference = mdatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference();

        messagesLv = (ListView) findViewById(R.id.messages_lv);
        messages = new ArrayList<>();

        // Get messages from database

        DatabaseReference messagesdbRef= mdbReference.child("message_log").child(messageLogId).child("messages");

        messagesdbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    //System.out.println(child.getValue());
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                messageAdapter = new MessageAdapter(getApplicationContext(),messages, senderUsername);
                messagesLv.setAdapter(messageAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_message_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.call_emergency:

                Intent callIntent = new Intent(Intent.ACTION_CALL);

                callIntent.setData(Uri.parse("tel:0221925995"));
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.CALL_PHONE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                startActivity(callIntent);
            }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private boolean checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }

    }
}
