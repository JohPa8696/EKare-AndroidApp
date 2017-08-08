package com.example.n.myfirstapplication.ui.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.n.myfirstapplication.AddContactDialogFragment;
import com.example.n.myfirstapplication.ItemInListView;
import com.example.n.myfirstapplication.ItemSeperator;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.Contact;
import com.example.n.myfirstapplication.ui.adapters.ContactAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactActivity extends Fragment {

    private FloatingActionButton addContact;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mContacts;
    private DatabaseReference mRequests;
    private DatabaseReference mUser;

    private Context mContext;

    private ContactAdapter contactAdapter;

    private ArrayList<ItemInListView> userRequests = new ArrayList<>();
    private ArrayList<ItemInListView> userContacts = new ArrayList<>();
    private ArrayList<ItemInListView> list = new ArrayList<>();

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_contact,container,false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mContacts = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("contacts");
        mRequests = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("requests");
        mUser = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());

        getActivity().setTitle("Contacts");
        mContext = getActivity().getApplicationContext();

        populateRequestList();

        mContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // update list view
                if(dataSnapshot.exists()) {
                    userContacts.clear();
                    userContacts.add(new ItemSeperator("Contacts"));

                    ArrayList<ItemInListView> tmpList = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Contact tmpUser = ds.getValue(Contact.class);
                        tmpList.add(new ItemInListView(tmpUser.getName(),tmpUser.getEmail(),
                                tmpUser.isMessagePermission(), tmpUser.isImagePermission()));
                    }

                    Collections.sort(tmpList, new Comparator<ItemInListView>() {
                        @Override
                        public int compare(ItemInListView o1, ItemInListView o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    userContacts.addAll(tmpList);
                    updateRequestList();
                }else{
                    userContacts.clear();
                    updateRequestList();
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
                    userRequests.clear();
                    userRequests.add(new ItemSeperator("Requests"));

                    ArrayList<ItemInListView> tmpList = new ArrayList<>();

                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        Contact tmpUser = ds.getValue(Contact.class);
                        tmpList.add(new ItemInListView(tmpUser.getName(),tmpUser.getEmail(),1));
                    }

                    Collections.sort(tmpList, new Comparator<ItemInListView>() {
                        @Override
                        public int compare(ItemInListView o1, ItemInListView o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    userRequests.addAll(tmpList);
                    updateRequestList();
                }else{
                    userRequests.clear();
                    updateRequestList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addContact = (FloatingActionButton) view.findViewById(R.id.addContactBtn);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });
        return view;
    }

    public void showAddContactDialog(){
        DialogFragment dialog = new AddContactDialogFragment();
        dialog.show(getActivity().getFragmentManager(), "AddContactDialog");
    }

    public void populateRequestList(){
        contactAdapter = new ContactAdapter(mContext, userRequests);
        ListView requestListView = (ListView) view.findViewById(R.id.requestList);
        requestListView.setAdapter(contactAdapter);
    }

    public void updateRequestList(){
        list.clear();
        list.addAll(userRequests);
        list.addAll(userContacts);
        contactAdapter.updateList(list);
    }

}
