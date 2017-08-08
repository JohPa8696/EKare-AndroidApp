package com.example.n.myfirstapplication.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnn on 8/9/2017.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter
{
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        titleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position){
        return titleList.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
