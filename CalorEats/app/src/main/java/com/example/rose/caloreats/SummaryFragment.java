package com.example.rose.caloreats;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SummaryFragment extends Fragment {
    // Store instance variables
    private String title;
    ArrayList<ArrayList<Food>> diary;

    // newInstance constructor for creating fragment with arguments
    public static SummaryFragment newInstance(String title,    ArrayList<ArrayList<Food>> diary_) {
        SummaryFragment summaryFragment = new SummaryFragment();
        summaryFragment.diary = diary_;
        Bundle args = new Bundle();
        args.putString("title", title);
        summaryFragment.setArguments(args);
        return summaryFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.weekly_summary, container, false);
        Graph weekly = view.findViewById(R.id.weekly_cal);
        weekly.setDiary(diary);
        weekly.invalidate();

        return view;
    }
}
