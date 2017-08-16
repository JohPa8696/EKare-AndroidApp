package com.example.n.myfirstapplication.untilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by johnn on 8/13/2017.
 */

public class FirebaseReferences {
    /**
     * Firebase authentication
     */
    public final static FirebaseAuth MY_AUTH = FirebaseAuth.getInstance();
    /**
     * Firebase storage
     */
    public final static FirebaseStorage MY_FIREBASE_STORAGE = FirebaseStorage.getInstance();
    /**
     * Firebase profile photo storage
     */
    public final static StorageReference PROFILE_IMAGE_STORAGE = MY_FIREBASE_STORAGE.getReference()
                                                                .child(FirebaseStrings.PROFILEPIC);
    /**
     * Firebase image accident storage
     */
    public final static StorageReference IMAGE_STORAGE = MY_FIREBASE_STORAGE.getReference()
                                                        .child(FirebaseStrings.IMAGE);
    /**
     * Firebase database
     */
    public final static FirebaseDatabase MY_FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    /**
     * Firebase user data node
     */
    public final static DatabaseReference USER_NODE = MY_FIREBASE_DATABASE.getReference()
                                                        .child(FirebaseStrings.USER);
    /**
     * Firebase contact node
     */
    public final static DatabaseReference CONTACT_NODE = USER_NODE.child(FirebaseStrings.CONTACTS);
    /**
     * Firebase message log node
     */
    public final static DatabaseReference MESSAGELOG_NODE = MY_FIREBASE_DATABASE.getReference()
                                                            .child(FirebaseStrings.MESSAGELOG);
    /**
     * Firebase message node
     */
    public final static DatabaseReference MESSAGE_NODE = MESSAGELOG_NODE.child(FirebaseStrings.MESSAGE);

}
