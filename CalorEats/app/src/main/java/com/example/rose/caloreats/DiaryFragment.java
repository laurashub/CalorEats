package com.example.rose.caloreats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiaryFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private int diaryIndex;
    ArrayList<ArrayList<Food>> diary;
    ArrayList<String> dates;

    // newInstance constructor for creating fragment with arguments
    public static DiaryFragment newInstance(String title,  ArrayList<ArrayList<Food>> diary_, ArrayList<String> dates_) {
        DiaryFragment diaryFragment = new DiaryFragment();
        diaryFragment.diary = diary_;
        diaryFragment.dates = dates_;
        Bundle args = new Bundle();
        args.putString("title", title);
        diaryFragment.setArguments(args);
        return diaryFragment;
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
        final View view = inflater.inflate(R.layout.food_diary, container, false);
        ListView listView = (ListView) view.findViewById(R.id.foodList);
        TextView dateView = (TextView) view.findViewById(R.id.date_text);

        dateView.setText("Date: " + dates.get(0));

        diaryIndex = 0;
        DiaryAdapter diaryAdapter = new DiaryAdapter(this.getContext(), diary.get(diaryIndex));
        listView.setAdapter(diaryAdapter);

        Button prev = (Button) view.findViewById(R.id.prev);
        Button next = (Button) view.findViewById(R.id.next);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateNaviClicked(view, true);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateNaviClicked(view, false);
            }
        });
        return view;
    }

    private void dateNaviClicked(View v3, boolean previous){

        ListView listView = (ListView) v3.findViewById(R.id.foodList);
        TextView dateView = (TextView) v3.findViewById(R.id.date_text);

        if (previous && diaryIndex != 6){
            diaryIndex++;
            DiaryAdapter switchDays = new DiaryAdapter(this.getContext(), diary.get(diaryIndex));
            listView.setAdapter(switchDays);
            dateView.setText("Date: " + dates.get(diaryIndex));
        } else if (!previous && diaryIndex != 0){
            diaryIndex--;
            DiaryAdapter switchDays = new DiaryAdapter(this.getContext(), diary.get(diaryIndex));
            listView.setAdapter(switchDays);
            dateView.setText("Date: " + dates.get(diaryIndex));
        } else {
            Toast.makeText(this.getContext(), "Unable to view that day.", Toast.LENGTH_SHORT).show();
        }

    }
}
