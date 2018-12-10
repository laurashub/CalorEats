package com.example.rose.caloreats;

//adapted from fc4

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import android.app.Activity;

import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter extends CursorAdapter {
    private Context mContext;
    private Activity mActivity;
    private SQLiteDatabase restaurantDB;


    public DatabaseAdapter(Context context, Cursor c, boolean autoRequery, Activity activity) {
        super(context, c, autoRequery);
        mContext = context;
        mActivity = activity;
    }

    public void setDatabase(SQLiteDatabase db_){
        restaurantDB= db_;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.food_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView foodName = (TextView) view.findViewById(R.id.food_name);
        TextView foodCalories = (TextView) view.findViewById(R.id.calories);
        TextView foodPrice = (TextView) view.findViewById(R.id.price);
        TextView foodRestaurant = (TextView) view.findViewById(R.id.restaurant);

        final String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        final String cals = cursor.getString(cursor.getColumnIndexOrThrow("calories"));
        final String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
        final String restaurantID = cursor.getString(cursor.getColumnIndexOrThrow("restaurant_id"));
        final String restaurant = getRestaurantName(restaurantID);

        foodName.setText(name);
        foodCalories.setText(cals);
        foodPrice.setText(price);
        foodRestaurant.setText(restaurant);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //only query again if you click the food

                Restaurant newRes = getRestaurant(restaurantID, restaurant);
                Intent newFood = new Intent(mContext, NewFood.class);
                newFood.putExtra("name", name);
                newFood.putExtra("cals", cals);
                newFood.putExtra("price", price);
                newFood.putExtra("restaurant", newRes);
                mActivity.startActivityForResult(newFood, 123);
            }
        });
    }

    public String getRestaurantName(String id){
        String table = "restaurants";
        String columns = "restaurants._id as _id, name, url";

        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();
        String queryString = "";

        where.add("(restaurants._id = ?)");
        args.add(id);

        if (where.size() != 0) {
            queryString += where.get(0);
            for (int i = 1; i < where.size(); i++) {
                queryString += " AND " + where.get(i);
            }
        }

        Cursor c = restaurantDB.query(table, columns.split(","), queryString,
                args.toArray(new String[0]), "", "", "");

        if (c != null) {
            if (c.getCount() != 1) {
                Toast.makeText(mContext, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                c.close();
                return "BAD";
            } else {
                c.moveToFirst();
                String restaurantName = c.getString(c.getColumnIndexOrThrow("name"));
                c.close();
                return restaurantName;
            }
        }
        return "RESTAURANT NOT FOUND";
    }

    public Restaurant getRestaurant(String id, String name){
        //get restaurant information
        Restaurant restaurantInfo = new Restaurant(Integer.parseInt(id), name);

        String table = "restaurants";
        String columns = "restaurants._id as _id, name, url";

        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();
        String queryString = "";

        where.add("(restaurants._id = ?)");
        args.add(id);

        if (where.size() != 0) {
            queryString += where.get(0);
            for (int i = 1; i < where.size(); i++) {
                queryString += " AND " + where.get(i);
            }
        }

        Cursor c = restaurantDB.query(table, columns.split(","), queryString,
                args.toArray(new String[0]), "", "", "");

        if (c != null) {
            if (c.getCount() != 1) {
                Toast.makeText(mContext, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
            } else {
                c.moveToFirst();
                String url = c.getString(c.getColumnIndexOrThrow("url"));
                restaurantInfo.setUrl(url);
            }
            c.close();
        }

        //get location information
        table = "locations";
        columns = "locations._id as _id, address, phone";

        where = new ArrayList<String>();
        args = new ArrayList<String>();
        queryString = "";

        where.add("(locations.restaurant_id = ?)");
        args.add(id);

        if (where.size() != 0) {
            queryString += where.get(0);
            for (int i = 1; i < where.size(); i++) {
                queryString += " AND " + where.get(i);
            }
        }

        c = restaurantDB.query(table, columns.split(","), queryString,
                args.toArray(new String[0]), "", "", "");

        ArrayList<String> addresses = new ArrayList<>();
        ArrayList<String> phoneNumbers = new ArrayList<>();


        if (c == null) {
            Toast.makeText(mContext, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
        } else {
            while(c.moveToNext()) {
                String address = c.getString(c.getColumnIndexOrThrow("address"));
                addresses.add(address);

                String phone = c.getString(c.getColumnIndexOrThrow("phone"));
                phoneNumbers.add(phone);
            }
        }

        restaurantInfo.setLocations(addresses);
        restaurantInfo.setPhoneNumbers(phoneNumbers);

        return restaurantInfo;
    }

}
