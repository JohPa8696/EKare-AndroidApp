package com.example.n.myfirstapplication;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.ui.activities.Main;
import com.example.n.myfirstapplication.untilities.ImageRounder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1123;
    static final int REQUEST_GALLARY = 3221;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
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
    private TextView mTermsAndConditions;

    private Spinner mGendersSpinner;
    private Spinner mRolesSpinner;
    private CheckBox mTermsContidionsCb;
    private Intent previousInt;

    private ImageRounder imageRounder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // Get firebase references
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
        mTermsAndConditions = (TextView) findViewById(R.id.termsandconditions_tv);

        // Add event listeners for buttons and imagebuttons
        mChooseFromGalleryIBtn.setOnClickListener(CreateAccount.this);
        mTakePictureIBtn.setOnClickListener(this);
        create = (Button) findViewById(R.id.createBtn);
        create.setOnClickListener(this);
        mTermsAndConditions.setOnClickListener(this);

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
            case R.id.termsandconditions_tv:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_termsandconditions,null);
                TextView tncTv = (TextView) mView.findViewById(R.id.tnc_tv);
                StringBuilder stringBuilder =new StringBuilder();
                try {
                    InputStream is = getAssets().open("files/termsandconditions.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line ="";
                    while((line = reader.readLine()) != null){
                        stringBuilder.append(line+"\n");
                    }
                    reader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tncTv.setMovementMethod(new ScrollingMovementMethod());
                tncTv.setText(stringBuilder.toString());
                builder.setView(mView);
                final AlertDialog alert = builder.create();
                Button backBtn = (Button) mView.findViewById(R.id.backBtn);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.cancel();
                    }
                });
                alert.show();
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
            mProfilePicture.setTag("Personal");
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
                mProfilePicture.setTag("Personal");
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

            Intent myintent = new Intent(this, Main.class);
            startActivity(myintent);
        }else{

        }
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

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
                            // If sign in fails, display a message_receiver.xml to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, task.getException().getCause().toString(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        // Check email field
        String email = mEmailField.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        //Check email confirm field
        String emailConfirm = mEmailConfirmField.getText().toString().trim();
        if(TextUtils.isEmpty(emailConfirm)){
            mEmailConfirmField.setError("Required.");
            valid =false;
        } else if (!emailConfirm.equals(email)){
            mEmailConfirmField.setError("Email do not match.");
            valid =false;
        } else {
            mEmailConfirmField.setError(null);
        }
        // Check password field
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        // Check password confirm field
        String passwordConfirm = mPasswordConfirmField.getText().toString().trim();
        if (TextUtils.isEmpty(passwordConfirm)) {
            mPasswordConfirmField.setError("Required.");
            valid = false;
        } else if(!passwordConfirm.equals(password)) {
            mPasswordConfirmField.setError("Password do not match.");
            valid = false;
        }else{
            mPasswordConfirmField.setError(null);
        }
        // Check name
        String firstName = mNameField.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            mNameField.setError("Required.");
            valid = false;
        } else {
            mNameField.setError(null);
        }
        // Check last name
        String lastName = mLastNameField.getText().toString().trim();
        if (TextUtils.isEmpty(lastName)) {
            mLastNameField.setError("Required.");
            valid = false;
        } else {
            mLastNameField.setError(null);
        }
        // Check phone
        String phone = mPhoneField.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            mPhoneField.setError("Required.");
            valid = false;
        } else {
            mPhoneField.setError(null);
        }
        // Check terms and conditions
        if(!mTermsContidionsCb.isChecked()){
            mTermsContidionsCb.setError("Please agree to the terms and conditions before proceed");
            valid = false;
        }else{
            mTermsContidionsCb.setError(null);
        }
        return valid;
    }

    private void addUser(String userId){
        if (!validateForm()) {
            return;
        }

        // Get the user information
        String fullName =mNameField.getText().toString().trim() + " " + mLastNameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();
        String phone = mPhoneField.getText().toString();
        String gender = mGendersSpinner.getSelectedItem().toString().trim();
        String role = mRolesSpinner.getSelectedItem().toString().trim();

        StorageReference profileRef;
        //Upload profile picture
        String tag = mProfilePicture.getTag().toString();
        final String[] profilePicUri= new String[1];
        if(tag.equals("default")){
            profileRef = mStorageRef.child("profile_pictures").child("default_profilepic.png");
            profilePicUri[0] = "default_profilepic.png";
        }else{
            Timestamp timeNow = new Timestamp(System.currentTimeMillis());
            profileRef = mStorageRef.child("profile_pictures").child(email.hashCode()+ timeNow.getTime() +".png");
            profilePicUri[0] = email.hashCode()+ timeNow.getTime() +".png";
            mProfilePicture.setDrawingCacheEnabled(true);
            mProfilePicture.buildDrawingCache();

            // Get bitmap from the circleimageview
            Bitmap bitmap = mProfilePicture.getDrawingCache();
            ByteArrayOutputStream stream = new ByteArrayOutputStream(bitmap.getByteCount());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data = stream.toByteArray();

            // Upload the picture to storage
            UploadTask uploadTask = (UploadTask) profileRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplication(), "Upload profile picture failed.", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplication(), "Upload profile picture success.", Toast.LENGTH_SHORT).show();
                }
            });

        }
        User newUser = new User(fullName,email,phone,gender,role,profilePicUri[0]);

        newUser.setDeviceToken(FirebaseInstanceId.getInstance().getToken());

        mDatabase.child("users").child(userId).setValue(newUser);
    }

}
