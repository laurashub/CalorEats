package com.example.rose.caloreats;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DiaryAdapter extends ArrayAdapter {
    private ArrayList<Food> foods;

    public DiaryAdapter(Context context, ArrayList<Food> foods_){

        super(context, 0, foods_);
        foods = foods_;

    }

    @Override
    public View getView(int position, View theView, ViewGroup parent) {

        Food food = (Food) getItem(position);
        if (theView == null) {
            theView = LayoutInflater.from(getContext()).inflate(R.layout.food_item, parent, false);
        }

        TextView name = (TextView) theView.findViewById(R.id.food_name);
        TextView cals = (TextView) theView.findViewById(R.id.calories);
        TextView price = (TextView) theView.findViewById(R.id.price);
        TextView restaurant = (TextView) theView.findViewById(R.id.restaurant);

        name.setText(food.getName());
        cals.setText(food.getCals());
        price.setText(food.getPrice());
        restaurant.setText(food.getRestaurant());

        return theView;
    }

    public void setItems(ArrayList<Food> newFoods) {
        this.foods.clear();
        this.foods.addAll(newFoods);
        notifyDataSetChanged();
    }
};