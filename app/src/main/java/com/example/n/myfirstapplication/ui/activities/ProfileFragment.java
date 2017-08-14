package com.example.n.myfirstapplication.ui.activities;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.n.myfirstapplication.R;

/***
 * ProfileFragment display the content in Profile Tab.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "User Profile";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile,container,false);
        getActivity().setTitle(TAG);
        return view;
    }

}
