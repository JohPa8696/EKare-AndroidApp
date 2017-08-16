package com.example.n.myfirstapplication.ui.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.n.myfirstapplication.Authentication;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.ui.adapters.SectionsPageAdapter;
import com.example.n.myfirstapplication.untilities.FirebaseReferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Main activity, tabs containers
 */
public class Main extends AppCompatActivity {

    private SectionsPageAdapter sectionAdapter;
    private ViewPager mViewPager;
    private Boolean allowMoveback=true;
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(allowMoveback);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                FirebaseReferences.MY_AUTH.signOut();
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        if(FirebaseReferences.MY_AUTH.getCurrentUser() == null){
            this.finish();
            Intent authActivity = new Intent(this, Authentication.class);
            authActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(authActivity);
        }else{
            Toast.makeText(this, "Logout failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
