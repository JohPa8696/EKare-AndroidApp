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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
                .setPositiveButton("Add Contact", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Add contact
                        addContact(email.getText().toString());
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

    public void addContact(){
        DatabaseReference contactRef = mUser.child("contacts").push();
        if (!email.getText().toString().equals("")){
            contactRef.setValue(email.getText().toString());
        }
    }

    // top level function to add contact
    public void addContact(final String emailString){

        mUser.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ToDo get current users email
                final String currentUserEmail = dataSnapshot.getValue(String.class);
                checkIfCurrentUser(emailString, currentUserEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    //check to prevent user from adding themselves
    public void checkIfCurrentUser(final String emailString, final String currentUserEmail){
        Query checkCurrentUser = mUser.orderByValue().equalTo(emailString);
        checkCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //User currentUser = (User) dataSnapshot.getValue(User.class);
                if (dataSnapshot.exists()){
                    //ToDo user feed back error
                    Toast.makeText(mContext, "Cannot add yourself!", Toast.LENGTH_SHORT).show();
                }else{
                    checkIfAdded(emailString, currentUserEmail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //check to prevent adding duplicate contacts
    public void checkIfAdded(final String emailString, final String currentUserEmail){
        //ToDo make a check so user cannot add themselves
        Query checkContactList = mUser.child("contacts").orderByValue().equalTo(emailString);
        checkContactList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    checkIfExists(emailString, currentUserEmail);
                }else{
                    //ToDo add user feedback
                    Toast.makeText(mContext, "User already in contacts!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //check to see if user exists in database
    public void checkIfExists(final String emailString, final String currentUserEmail){
        //tries to find the contact in the list of users and add to contacts
        Query findContactQuery = mDatabase.child("users").orderByChild("email").equalTo(emailString);
        findContactQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    DatabaseReference contactRef = mUser.child("contacts").push();
                    contactRef.setValue(emailString);

                    String key = contactRef.getKey();
                    String otherUserID = null;
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        otherUserID = childSnapshot.getKey();
                    }
                    //get the email to insert for the other persons
                    mDatabase.child("users").child(otherUserID).child("contacts").child(key).setValue(currentUserEmail);
                    Toast.makeText(mContext, "Added contact", Toast.LENGTH_SHORT).show();

                    // create message log
                    mDatabase.child("message-log").child(key).child("user1").setValue(currentUserEmail);
                    mDatabase.child("message-log").child(key).child("user2").setValue(emailString);


                }else{
                    //ToDo fix user feedback, when a user does not exist
                    Toast.makeText(mContext, "User does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
