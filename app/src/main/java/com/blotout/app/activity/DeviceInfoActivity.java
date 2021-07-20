package com.blotout.app.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blotout.app.R;
import com.blotout.app.adapter.ViewPagerAdapter;


public class DeviceInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addTitle("Location");
        adapter.addTitle("App");
        adapter.addTitle("Ads");
        adapter.addTitle("Battery");
        adapter.addTitle("Device");
        adapter.addTitle("Memory");
        adapter.addTitle("Network");
        adapter.addTitle("Installed Apps");
        adapter.addTitle("Contacts");
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
