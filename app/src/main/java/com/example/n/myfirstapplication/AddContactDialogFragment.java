package com.example.n.myfirstapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.n.myfirstapplication.dto.Contact;
import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.ui.adapter.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    private ArrayList<ItemInListView> items;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());
        mContext = getActivity();
        ArrayList<String> usersEmails = getAllUserEmails();


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_add_contact, null);

        final AutoCompleteTextView searchContact = (AutoCompleteTextView) layout.findViewById(R.id.searchAutoComplete);
        searchContact.setThreshold(1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, usersEmails);
        SearchAdapter searchAdapter = new SearchAdapter(mContext, getAllUsers());

        searchContact.setAdapter(adapter);

        builder.setView(layout)
                .setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send request to add contact
                        if(searchContact.getText().toString() != null && !searchContact.getText().toString().equals("")){
                            newRequest(searchContact.getText().toString());
                        }else{
                            Toast.makeText(mContext, "No user to add!", Toast.LENGTH_SHORT).show();
                        }

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

    public ArrayList<ItemInListView> getAllUsers(){
        final ArrayList<ItemInListView> items = new ArrayList<>();

        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Contact user = ds.getValue(Contact.class);
                    items.add(new ItemInListView(user.getName(), user.getEmail(), 4));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return items;
    }

    public ArrayList<String> getAllUserEmails(){
        final ArrayList<String> emails = new ArrayList<>();

        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Contact user = ds.getValue(Contact.class);
                    emails.add(user.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return emails;
    }
}
