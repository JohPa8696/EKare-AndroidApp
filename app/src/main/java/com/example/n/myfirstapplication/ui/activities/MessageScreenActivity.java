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
import android.widget.Toast;

import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.Message;
import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.ui.adapters.MessageAdapter;
import com.example.n.myfirstapplication.untilities.FirebaseReferences;
import com.example.n.myfirstapplication.untilities.FirebaseStrings;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/***
 * MessageScreenActivity displays the notification history
 */
public class MessageScreenActivity extends AppCompatActivity{
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    
    private ListView messagesLv;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private String messageLogID;
    private String contactPhone;
    private String contactEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);

        // Extract message_receiver.xml log id
        // Set the contact name in action bar
        Bundle mlID = getIntent().getExtras();
        String title = mlID.get("title").toString();
        setTitle(title);

        messagesLv = (ListView) findViewById(R.id.messages_lv);
        messages = new ArrayList<>();

        // Get list of messages
        messageLogID= mlID.get("id").toString();
        DatabaseReference messagesDBRef = FirebaseReferences.MESSAGELOG_NODE.child(messageLogID)
                                            .child(FirebaseStrings.MESSAGE);

        messagesDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }

                // Set the adapter
                messageAdapter = new MessageAdapter(getApplicationContext(),messages, contactEmail);
                messagesLv.setAdapter(messageAdapter);
                // Manually set the scroll to the bottom of the message_receiver.xml log
                messagesLv.setSelection(messageAdapter.getCount()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get the email of the contact
        String userID = FirebaseReferences.MY_AUTH.getCurrentUser().getUid();
        DatabaseReference contactEmailRef = FirebaseReferences.USER_NODE.child(userID)
                .child(FirebaseStrings.CONTACTS)
                .child(messageLogID)
                .child(FirebaseStrings.EMAIL);
        contactEmailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactEmail = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get the phone number of the contact
        DatabaseReference usersReference = FirebaseReferences.USER_NODE;
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    User user = child.getValue(User.class);
                    if(user.getEmail().trim().equals(contactEmail.trim())){
                        contactPhone = user.getPhone();
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
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }

        switch (item.getItemId()){
            case R.id.call_emergency:
                Intent callEmergencyIntent = new Intent(Intent.ACTION_CALL);
                callEmergencyIntent.setData(Uri.parse("tel:123"));
                startActivity(callEmergencyIntent);
                break;

            case R.id.call_person:
                Intent callPersonIntent = new Intent(Intent.ACTION_CALL);
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
                    Toast.makeText(this,"Request for cal'ing permission failed!",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
