package com.example.n.myfirstapplication.ui.activities;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.untilities.FirebaseReferences;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/***
 * ProfileFragment display the content in Profile Tab.
 * Author: Johnny
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "User Profile";

    private ImageButton nameEditBtn;
    private ImageButton phoneEditBtn;
    private ImageButton roleEditBtn;
    private ImageButton addressEditBtn;
    private ImageButton takePictureBtn;
    private ImageButton browsePictureBtn;
    private Button deleteAccountBtn;

    private CircleImageView profilePicture;
    private TextView nameTv;
    private TextView phoneTv;
    private TextView roleTv;
    private TextView locationTv;
    private TextView emailTv;

    private String profileUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile,container,false);
        getActivity().setTitle(TAG);

        // Obtains ui elements
        nameEditBtn = (ImageButton) view.findViewById(R.id.edit_name_btn);
        phoneEditBtn = (ImageButton) view.findViewById(R.id.edit_phone_btn);
        roleEditBtn = (ImageButton) view.findViewById(R.id.edit_role_btn);
        addressEditBtn = (ImageButton) view.findViewById(R.id.edit_address_btn);
        takePictureBtn = (ImageButton) view.findViewById(R.id.take_pic_btn);
        browsePictureBtn = (ImageButton) view.findViewById(R.id.choose_pic_btn);
        deleteAccountBtn = (Button) view.findViewById(R.id.delete_account_btn);

        profilePicture = (CircleImageView) view.findViewById(R.id.photo_profile_iv);
        nameTv = (TextView) view.findViewById(R.id.name_profile_tv);
        phoneTv = (TextView) view.findViewById(R.id.phone_profile_tv);
        roleTv = (TextView) view.findViewById(R.id.role_profile_tv);
        locationTv = (TextView) view.findViewById(R.id.address_profile_tv);
        emailTv = (TextView) view.findViewById(R.id.email_profile_tv);


        // Retrieve user details from database
        DatabaseReference userDatabaseRef = FirebaseReferences.USER_NODE
                                            .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid());
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                nameTv.setText(user.getName());
                phoneTv.setText(user.getPhone());
                roleTv.setText(user.getRole());
                locationTv.setText(user.getAddress());
                emailTv.setText(user.getEmail());
//                profileUri = user.getProfilePicUri();

                if(!user.getProfilePicUri().equals("default_profilepic.png")){
                    StorageReference profilePicRef = FirebaseReferences.PROFILE_IMAGE_STORAGE
                            .child(user.getProfilePicUri());
                    // Load the image using Glide
                    Glide.with(getContext())
                            .using(new FirebaseImageLoader())
                            .load(profilePicRef)
                            .into(profilePicture);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        if(profileUri.equals("default_profilepic.png")){
//            StorageReference profilePicRef = FirebaseReferences.PROFILE_IMAGE_STORAGE
//                    .child(profileUri);
//            // Load the image using Glide
//            Glide.with(view.getContext())
//                    .using(new FirebaseImageLoader())
//                    .load(profilePicRef)
//                    .into(profilePicture);
//        }
        return view;
    }

}
