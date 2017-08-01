package com.example.n.myfirstapplication;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.untilities.ImageRounder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1123;
    static final int REQUEST_GALLARY = 3221;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPhoneField;
    private EditText mPasswordField;
    private Button cancel;
    private Button create;

    private ImageButton mChooseFromGalleryIBtn;
    private ImageButton mTakePictureIBtn;
    private CircleImageView mProfilePicture;

    private EditText mLastNameField;
    private EditText mEmailConfirmField;
    private EditText mPasswordConfirmField;

    private Spinner mGendersSpinner;
    private Spinner mRolesSpinner;
    private CheckBox mTermsContidionsCb;
    private Intent previousInt;

    private ImageRounder imageRounder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Get firebase references
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get UI components
        mTakePictureIBtn = (ImageButton) findViewById(R.id.signup_take_pic);
        mChooseFromGalleryIBtn = (ImageButton) findViewById(R.id.signup_choose_pic);
        mProfilePicture = (CircleImageView) findViewById(R.id.signup_profile_pic);
        mNameField = (EditText) findViewById(R.id.field_name);
        mLastNameField = (EditText) findViewById(R.id.field_lastname);
        mEmailField = (EditText) findViewById(R.id.field_email);
        mEmailConfirmField = (EditText) findViewById(R.id.field_email_confirm);
        mPhoneField = (EditText) findViewById(R.id.field_phone);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mPasswordConfirmField =(EditText) findViewById(R.id.field_password_confirm);
        mGendersSpinner = (Spinner) findViewById(R.id.spinner_genders);
        mRolesSpinner =(Spinner) findViewById(R.id.spinner_role);
        mTermsContidionsCb =(CheckBox) findViewById(R.id.checkbox_tac);

        // Add event listeners for buttons and imagebuttons
        mChooseFromGalleryIBtn.setOnClickListener(this);
        mTakePictureIBtn.setOnClickListener(this);
        create = (Button) findViewById(R.id.createBtn);
        create.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(this);
        previousInt = new Intent(this, Authentication.class);
    }

    @Override
    public void onStart() {
        FirebaseAuth.getInstance().signOut();
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createBtn:
                createAccount(mEmailField.getText().toString(),
                        mPasswordField.getText().toString());
                break;
            case R.id.cancelBtn:
                startActivity(previousInt);
                break;
            case R.id.signup_choose_pic:
                Toast.makeText(this,"Pick a picture in gallery",Toast.LENGTH_SHORT).show();
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    openImageGallery();
                }else{
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions,REQUEST_GALLARY);
                }
                break;
            case R.id.signup_take_pic:
                Toast.makeText(this,"Take a profile picture",Toast.LENGTH_SHORT).show();
                if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ){
                    openCamera();
                } else {
                    String[] permissions ={Manifest.permission.CAMERA};
                    requestPermissions(permissions,REQUEST_IMAGE_CAPTURE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap picture = (Bitmap) extras.get("data");
            mProfilePicture.setImageBitmap(picture);
        }else if(requestCode == REQUEST_GALLARY && resultCode ==RESULT_OK){
            // This is the address of the image on the SD card
            Uri imageUri = data.getData();

            // Read the stream of data from SD card
            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(imageUri);

                // Get bitmap from the stream
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mProfilePicture.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
                Toast.makeText(this,"Cannot find image",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this,"Request camera permission failed!",Toast.LENGTH_SHORT).show();
                }
                return;
            case REQUEST_GALLARY:
                if(grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED ){
                    openImageGallery();
                }else{
                    Toast.makeText(this,"Request gallery permission failed!",Toast.LENGTH_SHORT).show();
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

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Toast.makeText(CreateAccount.this, "Authentication Success.",
                    Toast.LENGTH_SHORT).show();

            Intent myintent = new Intent(this, NavigationActivity.class);
            startActivity(myintent);
        }else{

        }
    }

    private void createAccount(String email, String password) {
        //Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // add user to database
                            addUser(user.getUid());

                            //Subscribe to topic
                            try {
                                FirebaseInstanceId.getInstance().deleteInstanceId();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String name = mNameField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mNameField.setError("Required.");
            valid = false;
        } else {
            mNameField.setError(null);
        }

        String phone = mPhoneField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPhoneField.setError("Required.");
            valid = false;
        } else {
            mPhoneField.setError(null);
        }
        return valid;
    }

    private void addUser(String userId){
        if (!validateForm()) {
            return;
        }

        User newUser = new User(
                mNameField.getText().toString(),
                mEmailField.getText().toString(),
                mPhoneField.getText().toString());

        newUser.setDeviceToken(FirebaseInstanceId.getInstance().getToken());

        mDatabase.child("users").child(userId).setValue(newUser);
    }

}
