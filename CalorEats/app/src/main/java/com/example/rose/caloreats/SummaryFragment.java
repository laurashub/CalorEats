package com.example.rose.caloreats;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import android.widget.TextView;

public class SummaryFragment extends Fragment {
    // Store instance variables
    private String title;
    UserData userData;

    // newInstance constructor for creating fragment with arguments
    public static SummaryFragment newInstance(String title, UserData userData_) {
        SummaryFragment summaryFragment = new SummaryFragment();
        summaryFragment.userData = userData_;
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

        TextView eaten = view.findViewById(R.id.eaten);
        TextView limit = view.findViewById(R.id.limit);

        Firestore.getInstance().getCalLimit(null, limit, null);

        Graph weekly = view.findViewById(R.id.weekly_cal);
        ArrayList<String> dates = Firestore.getInstance().getDateArray();

        int i = 0;
        for (String date : dates){

            if (i==0){
                Firestore.getInstance().getFoods(date, null, weekly, eaten);
                i++;
            }

            else{
                Firestore.getInstance().getFoods(date, null, weekly, null);
            }
        }


        return view;
    }
}
