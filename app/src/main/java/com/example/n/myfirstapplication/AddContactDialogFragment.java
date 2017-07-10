package com.example.n.myfirstapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by n on 10/07/2017.
 */

public class AddContactDialogFragment extends DialogFragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private EditText email;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());

        //email = (EditText) findViewById(R.id.textEmail);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_add_contact, null);

        builder.setView(layout)
                .setPositiveButton("Do action", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Add contact
                        addContact();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        AddContactDialogFragment.this.getDialog().cancel();
                    }
                });
        email = (EditText) layout.findViewById(R.id.textEmail);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void addContact(){
        DatabaseReference contactRef = mUser.child("contacts").push();
        if (!email.getText().toString().equals("")){
            contactRef.setValue(email.getText().toString());
        }


    }
}
