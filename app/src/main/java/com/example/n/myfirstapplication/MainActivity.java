package com.example.n.myfirstapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.example.n.myfirstapplication.ui.activities.MessageLogsActivity;
import com.example.n.myfirstapplication.ui.adapters.SectionsPageAdapter;

public class MainActivity extends AppCompatActivity{


    private SectionsPageAdapter sectionAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout layout = (TabLayout) findViewById(R.id.tabs);
        layout.setupWithViewPager(mViewPager);

    }

    public void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MessageLogsActivity(),"Conversations");
        viewPager.setAdapter(adapter);
    }
}
