package com.example.n.myfirstapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.myfirstapplication.ui.activities.Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.io.IOException;


public class Authentication extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private CheckBox mKeepMeLoginCb;
    private Button signup;
    private Button login;
    private Intent createAccountInt;

    private TextView logoName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_authentication);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mKeepMeLoginCb = (CheckBox) findViewById(R.id.remember_login_cb);
        createAccountInt = new Intent(this, CreateAccount.class);

        // Before doing anything check if there is a user account stored in the shared pref
        SharedPreferences sharedPref = getSharedPreferences("userAccount",Context.MODE_PRIVATE);

        String userEmail = sharedPref.getString("userEmail","");
        String userPassword = sharedPref.getString("userPassword","");
        if(!userEmail.trim().equals("") && !userPassword.trim().equals("")){
            mEmailField.setText(userEmail);
            mPasswordField.setText(userPassword);
            signIn(userEmail, userPassword);
        } else {
            signup = (Button) findViewById(R.id.SignupBtn);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(createAccountInt);
                }
            });

            login = (Button) findViewById(R.id.LoginBtn);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn(mEmailField.getText().toString().trim(),
                            mPasswordField.getText().toString().trim());
                }
            });

            logoName = (TextView) findViewById(R.id.logoname_tv);
            Typeface logoFont = Typeface.createFromAsset(this.getAssets(),"font/bungeeregular.ttf");
            logoName.setTypeface(logoFont);
            //Todo remove sign out user on start (for testing)
            //FirebaseAuth.getInstance().signOut();
        }

    }

    @Override
    public void onStart() {
        FirebaseAuth.getInstance().signOut();
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent mainIntent = new Intent(this, Main.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);

            // Save account details
            if (mKeepMeLoginCb.isChecked()){
                SharedPreferences sharePref = getSharedPreferences("userAccount", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharePref.edit();
                editor.putString("userEmail",mEmailField.getText().toString().trim().toLowerCase());
                editor.putString("userPassword",mPasswordField.getText().toString().trim());
                editor.apply();
                editor.commit();
            }
        }
    }


    private void signIn(String email, String password) {
        //Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            /*
                            //Subscribe to topic
                            //FirebaseInstanceId.getInstance().getToken();
                            Toast.makeText(Authentication.this, FirebaseInstanceId.getInstance().getToken(),
                                    Toast.LENGTH_SHORT).show();

                            updateUI(null);
                            FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
                            */
                            mDatabase.child("users").child(user.getUid()).child("deviceToken").setValue(FirebaseInstanceId.getInstance().getToken());
                            Log.d("TOKEN", FirebaseInstanceId.getInstance().getToken());
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message_receiver.xml to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Authentication.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
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

        return valid;
    }
}
