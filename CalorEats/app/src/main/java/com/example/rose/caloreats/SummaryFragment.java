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

    // newInstance constructor for creating fragment with arguments
    public static SummaryFragment newInstance(String title) {
        SummaryFragment summaryFragment = new SummaryFragment();
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

        ArrayList<TextView> days = new ArrayList<>();
        days.add((TextView) view.findViewById(R.id.day7));
        days.add((TextView) view.findViewById(R.id.day6));
        days.add((TextView) view.findViewById(R.id.day5));
        days.add((TextView) view.findViewById(R.id.day4));
        days.add((TextView) view.findViewById(R.id.day3));
        days.add((TextView) view.findViewById(R.id.day2));
        days.add((TextView) view.findViewById(R.id.day1));

        int i = 0;
        for (String date : dates){
            String[] splitDate = date.split("-");
            days.get(i).setText(splitDate[1] + "/" + splitDate[2]);
            System.out.println("Assigning day " + i + ": " + splitDate[1] + "/" + splitDate[2]);
            if (i==0){
                Firestore.getInstance().getFoods(date, null, weekly, eaten);
            }

            else{
                Firestore.getInstance().getFoods(date, null, weekly, null);
            }
            i++;
        }





        return view;
    }
}
