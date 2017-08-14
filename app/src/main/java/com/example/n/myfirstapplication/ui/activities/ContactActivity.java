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
import com.example.n.myfirstapplication.untilities.FirebaseReferences;
import com.example.n.myfirstapplication.untilities.FirebaseStrings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/***
 * Contact tab displays users contacts
 */
public class ContactActivity extends Fragment {
    private final static String TAG ="Manage contacts";
    private FloatingActionButton addContact;
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
        // Inflate the view using the activity contact layout
        view = inflater.inflate(R.layout.activity_contact,container,false);

        // Acquire Firebase login and database references.
        mContacts = FirebaseReferences.USER_NODE
                    .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid())
                    .child(FirebaseStrings.CONTACTS);
        mRequests = FirebaseReferences.USER_NODE.child("users")
                    .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid())
                    .child(FirebaseStrings.REQUESTS);
        mUser = FirebaseReferences.USER_NODE.child("users")
                .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid());

        // Set tab title
        getActivity().setTitle(TAG);
        mContext = getActivity().getApplicationContext();

        populateRequestList();

        // Listening to contacts changes in FB Database
        mContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Update list view
                if(dataSnapshot.exists()) {
                    userContacts.clear();
                    userContacts.add(new ItemSeperator("Contacts"));

                    ArrayList<ItemInListView> tmpList = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Contact tmpUser = ds.getValue(Contact.class);
                        tmpList.add(new ItemInListView(tmpUser.getName(),tmpUser.getEmail(),
                                tmpUser.isMessagePermission(), tmpUser.isImagePermission()));
                    }

                    // Sort the contacts in alphabetical order
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

        // Listening for new request in FB Database
        mRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Update list view
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

    /**
     * Open dialog and search for user.
     */
    public void showAddContactDialog(){
        DialogFragment dialog = new AddContactDialogFragment();
        dialog.show(getActivity().getFragmentManager(), "AddContactDialog");
    }

    /**
     * Set lister adapters
     */
    public void populateRequestList(){
        contactAdapter = new ContactAdapter(mContext, userRequests);
        ListView requestListView = (ListView) view.findViewById(R.id.requestList);
        requestListView.setAdapter(contactAdapter);
    }

    /**
     * Clear contacts and requests lists
     */
    public void updateRequestList(){
        list.clear();
        list.addAll(userRequests);
        list.addAll(userContacts);
        contactAdapter.updateList(list);
    }

}
