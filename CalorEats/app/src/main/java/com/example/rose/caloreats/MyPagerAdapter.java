package com.example.rose.caloreats;

//adapted from tutorial at https://guides.codepath.com/android/viewpager-with-fragmentpageradapter#overview

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;
    ArrayList<ArrayList<Food>> diary;
    ArrayList<String> dates;

    public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<ArrayList<Food>> diary_, ArrayList<String> dates_) {
        super(fragmentManager);
        diary = diary_;
        dates = dates_;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return QueryFragment.newInstance("Query");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return DiaryFragment.newInstance("Diary", diary, dates);
            case 2: // Fragment # 1 - This will show SecondFragment
                return SummaryFragment.newInstance( "Summary", diary);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
