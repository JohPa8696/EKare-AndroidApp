package com.example.n.myfirstapplication.ui.activities;

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

import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.Message;
import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.ui.adapters.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

    private String messageLogID;
    private String contactPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);

        // Extract message log id
        Bundle mlID = getIntent().getExtras();

        messageLogID= mlID.get("id").toString();
        // Set the contact name in action bar
        String title = mlID.get("title").toString();
        setTitle(title);

        mdatabase = FirebaseDatabase.getInstance();
        mdbReference = mdatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference();

        messagesLv = (ListView) findViewById(R.id.messages_lv);
        messages = new ArrayList<>();

        // Get messages from database

        DatabaseReference messagesDBRef= mdbReference.child("message_log").child(messageLogID).child("messages");

        messagesDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    //System.out.println(child.getValue());
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                messageAdapter = new MessageAdapter(getApplicationContext(),messages);
                messagesLv.setAdapter(messageAdapter);
                // Manually set the scroll to the bottom of the message log
                messagesLv.setSelection(messageAdapter.getCount()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // FOR APPENDING SINGLE MESSAGE WHEN ARRIVED - TESTING RIGHT NOW
        messagesDBRef.addChildEventListener(new ChildEventListener() {
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

        // Get the email of the contact
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference contactEmailRef = mdbReference.child("users").child(userID)
                .child("contacts").child(messageLogID).child("email");
        final String[] contactEmail={""};
        contactEmailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                        contactEmail[0]=child.getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Get the phone number of the contact
        DatabaseReference usersReference = mdbReference.child("users");
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    User user = child.getValue(User.class);
                    if(user.email.trim().equals(contactEmail[0].trim())){
                        contactPhone = user.phone;
                        break;
                    }
                }
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

        switch (item.getItemId()){
            case R.id.call_emergency:

                Intent callEmergencyIntent = new Intent(Intent.ACTION_CALL);

                callEmergencyIntent.setData(Uri.parse("tel:123"));
                startActivity(callEmergencyIntent);
                break;
            case R.id.call_person:
                final Intent callPersonIntent = new Intent(Intent.ACTION_CALL);
                //Get person phone number
                callPersonIntent.setData(Uri.parse("tel:"+contactPhone));
                startActivity(callPersonIntent);
                break;
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
}
