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
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryFragment extends Fragment {
    // Store instance variables
    private String title;
    private Context mContext;
    private MyPagerAdapter myPagerAdapter;

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

        Spinner restaurantSpinner = (Spinner) view.findViewById(R.id.restaurant);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.restaurants_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(adapter);

        ListView lv = (ListView) view.findViewById(R.id.searchResults);

        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            Log.e("DB", "Fail to create database");
        }
        final SQLiteDatabase restaurantDb = dbHelper.getReadableDatabase();

        final DatabaseAdapter da = new DatabaseAdapter(this.getContext(), null, false, this.getActivity());
        da.setDatabase(restaurantDb);

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
        // where contains the selection clause and args contains the corresponding arguments
        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();
        String queryString = "";

        String table = "foods";
        String columns = "foods._id as _id, name, calories, price, restaurant_id"; //attribute of that record

        Spinner spinner = (Spinner) v2.findViewById(R.id.restaurant);
        String restaurantName = spinner.getSelectedItem().toString();

        if (!restaurantName.equals("Nearby restaurants")){
            where.add("(foods.restaurant_id = ?)" );
            args.add(getRestaurantID(restaurantName, db));
        }

        EditText calLimitET = (EditText) v2.findViewById(R.id.calorie_limit);
        String calLimit = calLimitET.getText().toString();

        where.add("(foods.calories < ?)");
        args.add(calLimit);

        EditText priceLimitET = (EditText) v2.findViewById(R.id.price);
        String priceLimit = priceLimitET.getText().toString();

        where.add("(foods.price < ?)");
        args.add(priceLimit);

        if (where.size() != 0) {
            queryString += where.get(0);
            for (int i = 1; i < where.size(); i++) {
                queryString += " AND " + where.get(i);
            }
        }

        Log.d("query", queryString);

        Cursor c = db.query(table, columns.split(","), queryString,
                args.toArray(new String[0]), "", "", "calories");

        //update listview
        da.changeCursor(c);

        if (c.getCount()==0){
            Toast.makeText(getContext(), "No results found for that query!", Toast.LENGTH_SHORT).show();
        }

    }

    public String getRestaurantID(String restaurantName, SQLiteDatabase db){

        String table = "restaurants";
        String columns = "restaurants._id as _id, name, url";

        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();
        String queryString = "";

        where.add("(restaurants.name = ? )");
        args.add(restaurantName);

        if (where.size() != 0) {
            queryString += where.get(0);
            for (int i = 1; i < where.size(); i++) {
                queryString += " AND " + where.get(i);
            }
        }

        Cursor c = db.query(table, columns.split(","), queryString,
                args.toArray(new String[0]), "", "", "");

        if (c != null) {
            if (c.getCount() != 1) {
                Toast.makeText(getContext(), "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                return "BAD";
            } else {
                c.moveToFirst();
                String resID = c.getString(c.getColumnIndexOrThrow("_id"));
                return resID;
            }
        }
        return "RESTAURANT ID NOT FOUND";
    }

}
