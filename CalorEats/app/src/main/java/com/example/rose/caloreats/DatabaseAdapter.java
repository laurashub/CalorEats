package com.example.rose.caloreats;

//adapted from fc4

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import android.app.Activity;

import android.os.Bundle;

import android.database.Cursor;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.text.Html;

public class DatabaseAdapter extends CursorAdapter {
    Context mContext;
    Activity mActivity;


    public DatabaseAdapter(Context context, Cursor c, boolean autoRequery, Activity activity) {
        super(context, c, autoRequery);
        mContext = context;
        mActivity = activity;
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

        final String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        final String cals = cursor.getString(cursor.getColumnIndexOrThrow("calories"));
        final String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));

        /*
        int priceNum = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("price")));
        restaurantPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                v.getContext().startActivity(intent);
            }
        });
        restaurantUrl.setText(Html.fromHtml("<a href=\"" + url + "\">website</a>"));
        restaurantUrl.setMovementMethod(LinkMovementMethod.getInstance());*/

        foodName.setText(name);
        foodCalories.setText(cals);
        foodPrice.setText(price);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newFood = new Intent(mContext, NewFood.class);
                newFood.putExtra("name", name);
                newFood.putExtra("cals", cals);
                newFood.putExtra("price", price);
                mActivity.startActivityForResult(newFood, 123);
            }
        });
    }

}
