package com.example.rose.caloreats;

//adapted from tutorial at https://guides.codepath.com/android/viewpager-with-fragmentpageradapter#overview

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 4;
    private static String[] names = new String[]{"Suggestions","Query", "Diary", "Summary"};

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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
                return SuggestionsFragment.newInstance("Suggestions");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return QueryFragment.newInstance("Query");
            case 2: // Fragment # 1 - This will show SecondFragment
                return DiaryFragment.newInstance("Diary");
            case 3:
                return SummaryFragment.newInstance( "Summary");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return names[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

