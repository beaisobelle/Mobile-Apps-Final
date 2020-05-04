package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.example.afinal.ui.main.SectionsPagerAdapter;

import java.util.List;


public class MainApp extends AppCompatActivity implements SearchFragment.SendMessage {

    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main);
        //Creating SectionPageAdapter
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        //ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        //TabLayout
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void sendData(String message) {
        String tagStats = "android:switcher:" + R.id.view_pager + ":" + 1;
        StatsFragment f = (StatsFragment) getSupportFragmentManager().findFragmentByTag(tagStats);
        f.displayReceivedData(message);
    }


}
