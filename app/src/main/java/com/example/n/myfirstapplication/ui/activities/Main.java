package com.example.n.myfirstapplication.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.ui.adapters.SectionsPageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Main activity, tabs containers
 */
public class Main extends AppCompatActivity {

    private SectionsPageAdapter sectionAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sectionAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.main_tab_content);
        setupViewPager(mViewPager);

        TabLayout layout = (TabLayout) findViewById(R.id.tab_layout);
        layout.setupWithViewPager(mViewPager);
    }

    /***
     * Adding tabs for views
     * @param viewPager
     */
    public void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MessageLogsActivity(),"Notifications");
        adapter.addFragment(new ContactActivity(),"Contacts");
        adapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
    }
}
