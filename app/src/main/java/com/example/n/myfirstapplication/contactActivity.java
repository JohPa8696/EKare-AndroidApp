package com.example.n.myfirstapplication;

import android.app.DialogFragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.myfirstapplication.dto.Contact;
import com.example.n.myfirstapplication.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class contactActivity extends AppCompatActivity {

    private FloatingActionButton addContact;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mContacts;
    private DatabaseReference mRequests;
    private DatabaseReference mUser;

    private Context mContext;

    private HashSet<String> contactList = new HashSet<>();
    private ArrayAdapter<String> contactAdapter;

    private HashSet<String> requestList = new HashSet<>();
    private RequestArrayAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mContacts = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("contacts");
        mRequests = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("requests");
        mUser = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());

        mContext = contactActivity.this;

        initListView();
        populateRequestList();

        mContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // update list view
                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        contactList.add(ds.getValue().toString());
                    }
                    contactAdapter.clear();

                    ArrayList<String> listViewData = new ArrayList<>(contactList);
                    Collections.sort(listViewData);
                    contactAdapter.addAll(listViewData);
                    contactAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // update list view
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        requestList.add(ds.getValue().toString());
                    }
                    updateRequestList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRequests.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    requestList.add(ds.getValue().toString());
                }
                updateRequestList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addContact = (FloatingActionButton) findViewById(R.id.addContactBtn);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showAddContactDialog();
            }
        });
    }

    public void showAddContactDialog(){
        DialogFragment dialog = new AddContactDialogFragment();
        dialog.show(getFragmentManager(), "AddContactDialog");
    }

    public void initListView(){
        contactAdapter = new ArrayAdapter<>(
                this,
                R.layout.item_in_list_simple,
                new ArrayList<>(contactList)
        );

        ListView list = (ListView) findViewById(R.id.contactList);
        list.setAdapter(contactAdapter);
    }

    public void populateRequestList(){
        requestAdapter = new RequestArrayAdapter();
        ListView requestListView = (ListView) findViewById(R.id.requestList);
        requestListView.setAdapter(requestAdapter);
    }

    public void updateRequestList(){
        requestAdapter.clear();
        ArrayList<String> reqListViewData = new ArrayList<>(requestList);
        Collections.sort(reqListViewData);
        requestAdapter.addAll(reqListViewData);
        requestAdapter.updateList(reqListViewData);
        requestAdapter.notifyDataSetChanged();
    }

    private class RequestArrayAdapter extends ArrayAdapter<String>{
        private ArrayList<String> list;

        public RequestArrayAdapter(){
            super(contactActivity.this, R.layout.item_in_list_request, new ArrayList<>(requestList));
            this.list = new ArrayList<>(requestList);
        }

        private class ViewHolder{
            public TextView emailText;
            public Button addBtn;
            public Button ignoreBtn;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            ViewHolder holder = null;
            ListView requestListView = (ListView) findViewById(R.id.requestList);

            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_in_list_request, parent, false);

                holder = new ViewHolder();
                holder.emailText = (TextView) itemView.findViewById(R.id.emailText);
                holder.addBtn = (Button) itemView.findViewById(R.id.addBtn);
                holder.ignoreBtn = (Button) itemView.findViewById(R.id.ignoreBtn);
                itemView.setTag(holder);
            }else{
                holder = (ViewHolder) itemView.getTag();
            }
            // find item in list
            String email = list.get(position);
            final String emailToAdd = list.get(position);
            // Fill the view


            // Make


            //email
            TextView emailText = (TextView) itemView.findViewById(R.id.emailText);
            emailText.setText(email);

            //add button
            holder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addContactNew(emailToAdd);
                }
            });


            holder.ignoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeRequest(emailToAdd);
                }
            });

            return itemView;
        }

        public void updateList(ArrayList<String> list){
            this.list = list;
        }


        // Top Level function to add contact
        public void addContactNew(final String emailString){
            mUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final User currentUser = dataSnapshot.getValue(User.class);

                    if (!currentUser.getEmail().equals(currentUser)){ // prevent adding themselves
                        findContact(emailString, currentUser);
                    }else{
                        Toast.makeText(mContext, "Cannot add yourself", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // gets the other users key to allow for query
        public void findContact(final String emailString, final User currentUser){
            Query findContactQuery = mDatabase.child("users").orderByChild("email").equalTo(emailString);
            findContactQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String toastMessage = "Default";

                    if(dataSnapshot.exists()){
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            ds.getKey();
                            addContactNew(ds.getKey(), currentUser);
                        }
                    }else{
                        toastMessage = "User does not exist!";
                    }

                    Toast.makeText(mContext, toastMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void addContactNew(final String otherKey, final User currentUser){
            DatabaseReference otherUserRef = mDatabase.child("users").child(otherKey);
            otherUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String toastMessage = "Default";

                    if(dataSnapshot.exists()){
                        User otherUser = dataSnapshot.getValue(User.class);
                        HashMap<String, Contact> otherUserContacts = otherUser.getContacts();
                        HashMap<String, Contact> currentUserContacts = currentUser.getContacts();

                        Contact contactToCompare = new Contact();
                        contactToCompare.setEmail(otherUser.getEmail());

                        // Adds contact if it does not currently exist
                        if(currentUserContacts == null || !currentUserContacts.containsValue(contactToCompare)){ //check if contact exists
                            contactToCompare.setEmail(currentUser.getEmail());
                            if(otherUserContacts == null  || !otherUserContacts.containsValue(contactToCompare)){ //check if contact exists
                                // Generate unique key
                                DatabaseReference keyRef = mUser.child("contacts").push();
                                String key = keyRef.getKey();

                                // Create contacts to add
                                Contact otherContact = new Contact(otherUser.getName(), otherUser.getEmail(), true, true);
                                Contact currentContact = new Contact(currentUser.getName(), currentUser.getEmail(), true, true);

                                // add contacts to the users contact list
                                mUser.child("contacts").child(key).setValue(otherContact);
                                mDatabase.child("users").child(otherKey).child("contacts").child(key).setValue(currentContact);

                                // create message log between users
                                mDatabase.child("message_log").child(key).child("user1").setValue(otherContact.getEmail());
                                mDatabase.child("message_log").child(key).child("user2").setValue(currentContact.getEmail());

                                // remove the contact request
                                removeRequest(otherUser.getEmail());
                            }else{
                                toastMessage = "Contact already added!";
                            }
                        }else{
                            toastMessage = "Contact already added!";
                        }



                    }else{
                        toastMessage = "User does not exist!";
                    }

                    Toast.makeText(mContext, toastMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        //Remove request
        public void removeRequest(final String emailToRemove){

            Query removeRequestQuery = mRequests.orderByValue().equalTo(emailToRemove);
            removeRequestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String key = null;
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            key = childSnapshot.getKey();
                        }
                        //remove from database
                        mRequests.child(key).removeValue();
                    }else{
                        Toast.makeText(mContext, "Request does not exist!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
