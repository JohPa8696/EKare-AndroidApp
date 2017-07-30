package com.example.n.myfirstapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.myfirstapplication.ItemInListView;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.Contact;
import com.example.n.myfirstapplication.dto.User;
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
 * Created by n on 20/07/2017.
 */

public class ContactAdapter  extends BaseAdapter{
    private static final int TYPE_SEPARATOR = 0;
    private static final int TYPE_REQUEST = 1;
    private static final int TYPE_CONTACT = 2;
    private static final int TYPE_MAX_COUNT= 3;

    private ArrayList<ItemInListView> items;
    private LayoutInflater inflater;
    private Context mContext;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mContacts = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("contacts");
    private DatabaseReference mRequests = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("requests");
    private DatabaseReference mUser = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());

    public ContactAdapter(Context context, ArrayList<ItemInListView> items){
        this.mContext = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateList(ArrayList<ItemInListView> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position){
        return items.get(position).getType();
    }

    @Override
    public int getViewTypeCount(){
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);

        switch (type){
            case TYPE_SEPARATOR:
                if(convertView == null){
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_in_list_simple, null);
                    holder.title = (TextView) convertView.findViewById(R.id.simpleText);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.title.setText(items.get(position).getTitle());
                convertView.setOnClickListener(null);
                break;
            case TYPE_REQUEST:
                if(convertView == null){
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_in_list_request, null);
                    holder.emailText = (TextView) convertView.findViewById(R.id.emailText);
                    holder.nameText = (TextView) convertView.findViewById(R.id.nameText);
                    holder.addBtn = (Button) convertView.findViewById(R.id.addBtn);
                    holder.ignoreBtn = (Button) convertView.findViewById(R.id.ignoreBtn);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
                final String email = items.get(position).getEmail();
                final String name = items.get(position).getName();

                holder.emailText.setText(email);
                holder.nameText.setText(name);
                holder.addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addContactNew(email);
                    }
                });
                holder.ignoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeRequest(email);
                    }
                });
                break;
            case TYPE_CONTACT:
                if(convertView == null){
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_in_list_contact, null);
                    holder.emailText = (TextView) convertView.findViewById(R.id.emailText);
                    holder.nameText = (TextView) convertView.findViewById(R.id.nameText);
                    holder.messagesSwitch = (Switch) convertView.findViewById(R.id.messagesSwitch);
                    holder.imagesSwitch = (Switch) convertView.findViewById(R.id.imagesSwitch);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();

                }
                final String contactEmail = items.get(position).getEmail();
                final String contactName = items.get(position).getName();

                holder.emailText.setText(contactEmail);
                holder.nameText.setText(contactName);
                holder.messagesSwitch.setChecked(items.get(position).isMessagePermission());
                holder.imagesSwitch.setChecked(items.get(position).isImagePermission());

                holder.messagesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        toggleMessagePermission(contactEmail, isChecked);
                    }
                });

                holder.imagesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        toggleImagePermission(contactEmail, isChecked);
                    }
                });
                break;
        }

        return convertView;
    }


    public static class ViewHolder{
        public TextView title;
        public TextView nameText;
        public TextView emailText;
        public Button addBtn;
        public Button ignoreBtn;
        public Switch messagesSwitch;
        public Switch imagesSwitch;
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
        Query removeRequestQuery = mRequests.orderByChild("email").equalTo(emailToRemove);
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

    public void toggleMessagePermission(String email, final boolean permission){
        Query findContactQuery = mContacts.orderByChild("email").equalTo(email);
        findContactQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String key = null;
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        key = childSnapshot.getKey();
                    }
                    //toggle permission
                    mContacts.child(key).child("messagePermission").setValue(permission);
                    Toast.makeText(mContext, "Permissions have changed!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "Contact does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void toggleImagePermission(String email, final boolean permission){
        Query findContactQuery = mContacts.orderByChild("email").equalTo(email);
        findContactQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String key = null;
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        key = childSnapshot.getKey();
                    }
                    //toggle permission
                    mContacts.child(key).child("imagePermission").setValue(permission);
                    Toast.makeText(mContext, "Permissions have changed!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "Contact does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
