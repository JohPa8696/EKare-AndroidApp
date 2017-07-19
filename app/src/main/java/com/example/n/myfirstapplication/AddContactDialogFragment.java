package com.example.n.myfirstapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.myfirstapplication.dto.Contact;
import com.example.n.myfirstapplication.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by n on 10/07/2017.
 */

public class AddContactDialogFragment extends DialogFragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mCurrentUser;
    private EditText email;
    private Context mContext;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());
        mContext = getActivity();


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_add_contact, null);
        email = (EditText) layout.findViewById(R.id.textEmail);

        builder.setView(layout)
                .setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send request to add contact
                        newRequest(email.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        AddContactDialogFragment.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    // Gets the current user object
    public void newRequest(final String userToAdd){
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User currentUser = dataSnapshot.getValue(User.class);
                if(!currentUser.getEmail().equals(userToAdd)){
                    newRequest(userToAdd, currentUser);
                }else{
                    Toast.makeText(mContext, "Cannot add yourself!", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Gets the other users ID
    public void newRequest(final String userToAdd, final User currentUser){
        Query findContactQuery = mDatabase.child("users").orderByChild("email").equalTo(userToAdd);
        findContactQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String otherUserID = null;
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        otherUserID = childSnapshot.getKey();
                    }
                    newRequest(userToAdd, currentUser, otherUserID);

                }else{
                    Toast.makeText(mContext, "User does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Sends a request to the other user
    public void newRequest(final String userToAdd, final User currentUser, final String otherUserID){
        DatabaseReference otherUserRef = mDatabase.child("users").child(otherUserID);
        otherUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //get the email to insert for the other persons

                    User otherUser = dataSnapshot.getValue(User.class);


                    Contact currentUserContact = new Contact(currentUser.getName(), currentUser.getEmail() ,true , true);
                    Contact otherUserContact = new Contact(otherUser.getName(), otherUser.getEmail(), true, true);

                    HashMap<String, Contact> currentUserContactList = currentUser.getContacts();
                    HashMap<String, Contact> otherUserContactList = otherUser.getContacts();

                    HashMap<String, Contact> currentUserRequestList = currentUser.getRequests();
                    HashMap<String, Contact> otherUserRequestList = otherUser.getRequests();

                    if((currentUserContactList == null || !currentUserContactList.containsValue(otherUserContact))
                            && (otherUserContactList == null || !otherUserContactList.containsValue(currentUserContact))
                            && (currentUserRequestList == null || !currentUserRequestList.containsValue(otherUserContact))
                            && (otherUserRequestList == null || !otherUserRequestList.containsValue(currentUserContact))){

                        DatabaseReference requestRef = mDatabase.child("users").child(otherUserID).child("requests").push();
                        requestRef.setValue(currentUserContact);

                        Toast.makeText(mContext, "Request Sent!", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(mContext, "Failed user already contact or request already sent", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(mContext, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // top level function to request for contact to be added
    public void requestToAddContact(final String userToAdd){
        //get my email
        //check no to add self
        //check my contacts - no duplicates
        //*check if request exists
        //check user exists
        mUser.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String currentUser = dataSnapshot.getValue(String.class);
                reqCheckIfSelf(userToAdd, currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void reqCheckIfSelf(final String userToAdd, final String currentUser){
        Query checkCurrentUser = mUser.orderByValue().equalTo(userToAdd);
        checkCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(mContext, "Cannot add yourself!", Toast.LENGTH_SHORT).show();
                }else{
                    reqCheckIfAdded(userToAdd, currentUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void reqCheckIfAdded(final String userToAdd, final String currentUser){
        Query checkContactList = mUser.child("contacts").orderByValue().equalTo(userToAdd);
        checkContactList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    reqCheckIfUserExists(userToAdd, currentUser);
                }else{
                    Toast.makeText(mContext, "User already in contacts!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void reqCheckIfUserExists(final String userToAdd, final String currentUser){
        //tries to find the contact in the list of users and add to contacts
        Query findContactQuery = mDatabase.child("users").orderByChild("email").equalTo(userToAdd);
        findContactQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String otherUserID = null;
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        otherUserID = childSnapshot.getKey();
                    }
                    reqCheckIfRequestExists(userToAdd, currentUser, otherUserID);

                }else{
                    Toast.makeText(mContext, "User does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //ToDo check if request exists for either user
    public void reqCheckIfRequestExists(final String userToAdd, final String currentUser, final String otherUserID){
        //ToDo implement method
        Query findContactQuery = mDatabase.child("users").child(otherUserID).child("requests").orderByValue().equalTo(currentUser);
        findContactQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    //get the email to insert for the other persons
                    DatabaseReference requestRef = mDatabase.child("users").child(otherUserID).child("requests").push();
                    requestRef.setValue(currentUser);

                    Toast.makeText(mContext, "Request Sent!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(mContext, "Request already sent!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
