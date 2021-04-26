package com.blotout.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.blotout.app.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    @NonNull
    List<String> titles = new ArrayList<>();
    public ViewPagerAdapter(@NonNull FragmentManager manager) {
        super(manager);
    }

    public void addTitle(String title) {
        titles.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MainFragment.newInstance(position);
    }


    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
