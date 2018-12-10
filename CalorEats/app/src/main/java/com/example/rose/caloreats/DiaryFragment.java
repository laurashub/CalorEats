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

//diary fragment, controls diary screen

public class DiaryFragment extends Fragment {
    // Store instance variables
    private int diaryIndex;
    private ArrayList<String> dates;

    // newInstance constructor for creating fragment with arguments
    public static DiaryFragment newInstance(String title) {
        DiaryFragment diaryFragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        diaryFragment.setArguments(args);
        return diaryFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.food_diary, container, false);

        diaryIndex = 0;

        dates = Firestore.getInstance().getDateArray();

        TextView limit = view.findViewById(R.id.limit);
        Firestore.getInstance().getCalLimit(null, limit, null);

        switchDate(view);

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

        if (previous && diaryIndex != 6){
            diaryIndex++;
            switchDate(v3);
        } else if (!previous && diaryIndex != 0){
            diaryIndex--;
            switchDate(v3);
        } else {
            Toast.makeText(this.getContext(), "Unable to view that day.", Toast.LENGTH_SHORT).show();
        }

    }

    private void switchDate(View v){
        ListView listView = (ListView) v.findViewById(R.id.foodList);
        TextView dateView = (TextView) v.findViewById(R.id.date_text);

        TextView eaten = v.findViewById(R.id.eaten);
        Firestore.getInstance().getCalLimit(null, (TextView) v.findViewById(R.id.limit), null);

        dateView.setText("Date: " + dates.get(diaryIndex));
        DiaryAdapter da = new DiaryAdapter(getContext(), new ArrayList<Food>());
        Firestore.getInstance().getFoods(dates.get(diaryIndex), da, null, eaten, null);
        listView.setAdapter(da);
    }
}
