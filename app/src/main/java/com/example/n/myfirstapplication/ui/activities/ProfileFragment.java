package com.example.n.myfirstapplication.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.n.myfirstapplication.Authentication;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.untilities.FirebaseReferences;
import com.example.n.myfirstapplication.untilities.FirebaseStrings;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/***
 * ProfileFragment display the content in Profile Tab.
 * Author: Johnny
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "User Profile";
    static final int REQUEST_IMAGE_CAPTURE = 1123;
    static final int REQUEST_GALLARY = 3221;

    private ImageButton nameEditBtn;
    private ImageButton phoneEditBtn;
    private ImageButton roleEditBtn;
    private ImageButton addressEditBtn;
    private ImageButton takePictureBtn;
    private ImageButton browsePictureBtn;
    private Button deleteAccountBtn;
    private Button saveChangesBtn;

    private CircleImageView profilePicture;
    private TextView nameTv;
    private TextView phoneTv;
    private TextView roleTv;
    private TextView locationTv;
    private TextView emailTv;

    private boolean isProfilePicChanged = false;

    private String profileUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile,container,false);
        //getActivity().setTitle(TAG);

        // Obtains ui elements
        nameEditBtn = (ImageButton) view.findViewById(R.id.edit_name_btn);
        phoneEditBtn = (ImageButton) view.findViewById(R.id.edit_phone_btn);
        roleEditBtn = (ImageButton) view.findViewById(R.id.edit_role_btn);
        addressEditBtn = (ImageButton) view.findViewById(R.id.edit_address_btn);
        takePictureBtn = (ImageButton) view.findViewById(R.id.take_pic_btn);
        browsePictureBtn = (ImageButton) view.findViewById(R.id.choose_pic_btn);
        deleteAccountBtn = (Button) view.findViewById(R.id.delete_account_btn);
        saveChangesBtn = (Button) view.findViewById(R.id.save_changes_btn);

        profilePicture = (CircleImageView) view.findViewById(R.id.photo_profile_iv);
        nameTv = (TextView) view.findViewById(R.id.name_profile_tv);
        phoneTv = (TextView) view.findViewById(R.id.phone_profile_tv);
        roleTv = (TextView) view.findViewById(R.id.role_profile_tv);
        locationTv = (TextView) view.findViewById(R.id.address_profile_tv);
        emailTv = (TextView) view.findViewById(R.id.email_profile_tv);

        takePictureBtn.setOnClickListener(this);
        browsePictureBtn.setOnClickListener(this);
        nameEditBtn.setOnClickListener(this);
        phoneEditBtn.setOnClickListener(this);
        roleEditBtn.setOnClickListener(this);
        addressEditBtn.setOnClickListener(this);
        deleteAccountBtn.setOnClickListener(this);
        saveChangesBtn.setOnClickListener(this);

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
                profileUri = user.getProfilePicUri();

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

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.take_pic_btn:
                Toast.makeText(getActivity(),"Take a profile picture",Toast.LENGTH_SHORT).show();
                if(getContext().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ){
                    openCamera();
                } else {
                    String[] permissions ={Manifest.permission.CAMERA};
                    requestPermissions(permissions,REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.choose_pic_btn:
                Toast.makeText(getContext(),"Pick a picture in gallery",Toast.LENGTH_SHORT).show();
                if(getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    openImageGallery();
                }else{
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions,REQUEST_GALLARY);
                }
                break;
            case R.id.edit_name_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit,null);
                final EditText editText = (EditText) mView.findViewById(R.id.edit_et);
                editText.setHint("Full Name");
                editText.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                builder.setView(mView);
                final AlertDialog alert = builder.create();
                Button saveBtn = (Button) mView.findViewById(R.id.edit_save_btn);
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nameTv.setText(editText.getText().toString().trim());
                        Toast.makeText(getContext(),"Set name to " +editText.getText().toString(),Toast.LENGTH_SHORT).show();
                        alert.cancel();
                    }
                });
                alert.show();
                break;
            case R.id.edit_phone_btn:
                AlertDialog.Builder builderPhone = new AlertDialog.Builder(getContext());
                View mPhoneView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit,null);
                final EditText phoneEditText = (EditText) mPhoneView.findViewById(R.id.edit_et);
                phoneEditText.setHint("Phone number");
                phoneEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                builderPhone.setView(mPhoneView);
                final AlertDialog alertPhone = builderPhone.create();
                Button savePhoneBtn = (Button) mPhoneView.findViewById(R.id.edit_save_btn);
                savePhoneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        phoneTv.setText(phoneEditText.getText().toString().trim());
                        Toast.makeText(getContext(),"Set phone number to " +phoneEditText.getText().toString(),Toast.LENGTH_SHORT).show();
                        alertPhone.cancel();
                    }
                });
                alertPhone.show();
                break;
            case R.id.edit_address_btn:
                AlertDialog.Builder builderAddress = new AlertDialog.Builder(getContext());
                View mAddressView = getActivity().getLayoutInflater().inflate(R.layout.dialog_address,null);
                final EditText address1 = (EditText) mAddressView.findViewById(R.id.edit_ad1_et);
                final EditText address2 = (EditText) mAddressView.findViewById(R.id.edit_ad2_et);
                final EditText city = (EditText) mAddressView.findViewById(R.id.edit_ad3_et);
                final EditText state = (EditText) mAddressView.findViewById(R.id.edit_ad4_et);
                final EditText postcode = (EditText) mAddressView.findViewById(R.id.edit_ad5_et);

                builderAddress.setView(mAddressView);
                final AlertDialog alertAddress = builderAddress.create();
                Button saveAddressBtn = (Button) mAddressView.findViewById(R.id.edit_address_save_btn);
                saveAddressBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        locationTv.setText(address1.getText().toString().trim() + ", " +
                                address2.getText().toString().trim() + ", "+
                                city.getText().toString().trim() + ", "+
                                state.getText().toString().trim() + ", "+
                                postcode.getText().toString().trim() );
                        Toast.makeText(getContext(),"Set address number to "
                                +locationTv.getText(),Toast.LENGTH_SHORT).show();
                        alertAddress.cancel();
                    }
                });
                alertAddress.show();
                break;
            case R.id.edit_role_btn:
                AlertDialog.Builder builderRole = new AlertDialog.Builder(getContext());
                View mRoleView = getActivity().getLayoutInflater().inflate(R.layout.dialog_spinner,null);
                final Spinner roleSpinner = (Spinner) mRoleView.findViewById(R.id.edit_role_sp);
                builderRole.setView(mRoleView);
                final AlertDialog alertRole = builderRole.create();
                Button saveRoleBtn = (Button) mRoleView.findViewById(R.id.edit_save_btn);
                saveRoleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        roleTv.setText(roleSpinner.getSelectedItem().toString().trim());
                        Toast.makeText(getContext(),"Set role number to "
                                +roleSpinner.getSelectedItem().toString().trim(),Toast.LENGTH_SHORT).show();
                        alertRole.cancel();
                    }
                });
                alertRole.show();
                break;
            case R.id.save_changes_btn:
                // Save the changes to database
                // Set name
                FirebaseReferences.USER_NODE
                        .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid())
                        .child(FirebaseStrings.NAME).setValue(nameTv.getText().toString().trim());
                // Set phone
                FirebaseReferences.USER_NODE
                        .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid())
                        .child(FirebaseStrings.PHONE).setValue(phoneTv.getText().toString().trim());
                // Set role
                FirebaseReferences.USER_NODE
                        .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid())
                        .child(FirebaseStrings.ROLE).setValue(roleTv.getText().toString().trim());
                // Set address
                FirebaseReferences.USER_NODE
                        .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid())
                        .child(FirebaseStrings.ADDRESS).setValue(locationTv.getText().toString().trim());

                // Update profile photo uri and upload
                if(isProfilePicChanged) {
                    Timestamp timeNow = new Timestamp(System.currentTimeMillis());
                    final String photoUri = emailTv.getText().toString().trim().hashCode() + timeNow.getTime() + ".png";
                    StorageReference profileRef = FirebaseReferences.PROFILE_IMAGE_STORAGE.child(photoUri);

                    profilePicture.setDrawingCacheEnabled(true);
                    profilePicture.buildDrawingCache();

                    // Get bitmap from the circleimageview
                    Bitmap bitmap = profilePicture.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream(bitmap.getByteCount());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] data = stream.toByteArray();

                    // Upload the picture to storage
                    UploadTask uploadTask = (UploadTask) profileRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getContext(), "Upload profile picture failed.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseReferences.USER_NODE
                                    .child(FirebaseReferences.MY_AUTH.getCurrentUser().getUid())
                                    .child(FirebaseStrings.PROFILEPIC).setValue(photoUri);
                            Toast.makeText(getContext(), "Upload profile picture success.", Toast.LENGTH_SHORT).show();
                            isProfilePicChanged = false;
                        }
                    });
                }
                break;
            case R.id.delete_account_btn:
                AlertDialog.Builder builderDelete = new AlertDialog.Builder(getContext());
                View mDeleteView = getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_account,null);
                final EditText emailEt = (EditText) mDeleteView.findViewById(R.id.email_et);
                final EditText passwordEt = (EditText) mDeleteView.findViewById(R.id.password_et);

                builderDelete.setView(mDeleteView);
                final AlertDialog alertDelete = builderDelete.create();
                Button deleteBtn = (Button) mDeleteView.findViewById(R.id.delete_account_btn);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteUser(emailEt.getText().toString().trim(),passwordEt.getText().toString().trim());
                        alertDelete.cancel();
                    }
                });
                alertDelete.show();

                Button cancelbtn =(Button) mDeleteView.findViewById(R.id.cancel_btn);
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDelete.cancel();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap picture = (Bitmap) extras.get("data");
            profilePicture.setImageBitmap(picture);
            isProfilePicChanged = true;
            profilePicture.setTag("Personal");
        }else if(requestCode == REQUEST_GALLARY && resultCode ==RESULT_OK){
            // This is the address of the image on the SD card
            Uri imageUri = data.getData();

            // Read the stream of data from SD card
            InputStream inputStream;
            try {
                inputStream = getContext().getContentResolver().openInputStream(imageUri);

                // Get bitmap from the stream
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profilePicture.setImageBitmap(bitmap);
                isProfilePicChanged = true;
                profilePicture.setTag("Personal");
            }catch (FileNotFoundException e){
                e.printStackTrace();
                Toast.makeText(getContext(),"Cannot find image",Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }else{
                    Toast.makeText(getContext(),"Request camera permission failed!",Toast.LENGTH_SHORT).show();
                }
                return;
            case REQUEST_GALLARY:
                if(grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED ){
                    openImageGallery();
                }else{
                    Toast.makeText(getContext(),"Request gallery permission failed!",Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void openCamera(){
        Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
    }
    private void openImageGallery(){
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);

        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picDirPath = imageDir.getPath();
        Uri data =Uri.parse(picDirPath);
        pickImageIntent.setDataAndType(data,"image/*");
        startActivityForResult(pickImageIntent,REQUEST_GALLARY);
    }
    private void deleteUser(String email, String password){
        final FirebaseUser user = FirebaseReferences.MY_AUTH.getCurrentUser();

        // User credential
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Revert to login screen
                        SharedPreferences sharePref =  getActivity().getSharedPreferences("userAccount", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharePref.edit();
                        editor.clear();
                        editor.commit();
                        getActivity().finish();
                        Intent authActivity = new Intent(getActivity(), Authentication.class);
                        authActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(authActivity);
//
//                        // Get user data references
//                        final User userInfo;
//                        FirebaseReferences.USER_NODE.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                User userInfo =  dataSnapshot.getValue(User.class);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                        // Delete user's entry in the user node
//                        FirebaseReferences.USER_NODE.child(user.getUid()).removeValue();

                        // Delete user authentication
                        Toast.makeText(getContext(),"User account deleted!",Toast.LENGTH_SHORT).show();
                        user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(),"User account deleted!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }
                });
    }

}
