package com.example.rose.caloreats;

//code adapted from https://www.journaldev.com/10096/android-viewpager-example-tutorial

import android.app.Activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;

import android.util.Log;

enum ModelObject {

    SUGGESTIONS(R.string.suggestions, R.layout.suggestion_layout),
    SEARCH(R.string.search, R.layout.search_layout),
    DIARY(R.string.diary, R.layout.food_diary),
    SUMMARY(R.string.summary, R.layout.weekly_summary);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private Activity mActivity;
    private ArrayList<ArrayList<Food>> diary;
    private ArrayList<String> dates;
    int diaryIndex;

    public CustomPagerAdapter(Context context, Activity activity,
                              ArrayList<String> dates_, ArrayList<ArrayList<Food>> diary_) {
        mContext = context;
        mActivity = activity;
        diary = diary_;
        dates = dates_;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObject modelObject = ModelObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);

        switch(modelObject.getTitleResId()){
            case R.string.search:

                final View v2 = inflater.inflate(modelObject.getLayoutResId(), null);
                ListView lv = (ListView) v2.findViewById(R.id.searchResults);

                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                try {
                    dbHelper.createDatabase();
                } catch (IOException e) {
                    Log.e("DB", "Fail to create database");
                }
                final SQLiteDatabase restaurantDb = dbHelper.getReadableDatabase();

                final DatabaseAdapter da = new DatabaseAdapter(mContext, null, false, mActivity);

                lv.setAdapter(da);

                Button go = (Button) v2.findViewById(R.id.go);
                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queryClicked(v2, da, restaurantDb);
                    }
                });
                collection.addView(v2);
                return v2;


            case R.string.diary:

                final View v3 = inflater.inflate(modelObject.getLayoutResId(), null);
                ListView listView = (ListView) v3.findViewById(R.id.foodList);
                TextView dateView = (TextView) v3.findViewById(R.id.date_text);

                dateView.setText("Date: " + dates.get(0));

                diaryIndex = 0;
                DiaryAdapter diaryAdapter = new DiaryAdapter(mContext, diary.get(diaryIndex));
                listView.setAdapter(diaryAdapter);

                Button prev = (Button) v3.findViewById(R.id.prev);
                Button next = (Button) v3.findViewById(R.id.next);
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateNaviClicked(v3, true);
                    }
                });

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateNaviClicked(v3, false);
                    }
                });


                collection.addView(v3);
                return v3;

            case R.string.summary:
                final View v4 = inflater.inflate(modelObject.getLayoutResId(), null);
                Graph weekly = v4.findViewById(R.id.weekly_cal);
                weekly.setDiary(diary);
                weekly.invalidate();

        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return ModelObject.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelObject customPagerEnum = ModelObject.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

    //Helper methods
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

    private void dateNaviClicked(View v3, boolean previous){

        ListView listView = (ListView) v3.findViewById(R.id.foodList);
        TextView dateView = (TextView) v3.findViewById(R.id.date_text);

        if (previous && diaryIndex != 6){
            diaryIndex++;
            DiaryAdapter switchDays = new DiaryAdapter(mContext, diary.get(diaryIndex));
            listView.setAdapter(switchDays);
            dateView.setText("Date: " + dates.get(diaryIndex));
        } else if (!previous && diaryIndex != 0){
            diaryIndex--;
            DiaryAdapter switchDays = new DiaryAdapter(mContext, diary.get(diaryIndex));
            listView.setAdapter(switchDays);
            dateView.setText("Date: " + dates.get(diaryIndex));
        } else {
            Toast.makeText(mContext, "Unable to view that day.", Toast.LENGTH_SHORT).show();
        }

    }

}