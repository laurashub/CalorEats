package com.example.rose.caloreats;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.app.Activity;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryFragment extends Fragment {
    // Store instance variables
    private String title;
    private Context mContext;

    // newInstance constructor for creating fragment with arguments
    public static QueryFragment newInstance(String title) {
        QueryFragment queryFragment = new QueryFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        queryFragment.setArguments(args);
        return queryFragment;
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
        final View view = inflater.inflate(R.layout.search_layout, container, false);
        ListView lv = (ListView) view.findViewById(R.id.searchResults);

        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            Log.e("DB", "Fail to create database");
        }
        final SQLiteDatabase restaurantDb = dbHelper.getReadableDatabase();

        final DatabaseAdapter da = new DatabaseAdapter(this.getContext(), null, false, this.getActivity());

        lv.setAdapter(da);

        Button go = (Button) view.findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryClicked(view, da, restaurantDb);
            }
        });

        return view;
    }

    private void queryClicked(View v2, DatabaseAdapter da, SQLiteDatabase db) {
        // where contains the selection clause and args contains the coresponding arguments
        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();
        String queryString = "";

        String table = "foods";
        String columns = "foods._id as _id, name, calories, price, restaurant_id"; //attribute of that record

        EditText calLimitET = (EditText) v2.findViewById(R.id.calorie_limit);
        String calLimit = calLimitET.getText().toString();

        where.add("(foods.calories < ?)");
        args.add(calLimit);

        if (where.size() != 0) {
            queryString += where.get(0);
            for (int i = 1; i < where.size(); i++) {
                queryString += " AND " + where.get(i);
            }
        }

        Log.d("query", queryString);

        Cursor c = db.query(table, columns.split(","), queryString,
                args.toArray(new String[0]), "", "", "");

        //update listview
        da.changeCursor(c);

        if (c.getCount()==0){
            Toast.makeText(mContext, "No results found for that query!", Toast.LENGTH_SHORT).show();
        }

    }
}
