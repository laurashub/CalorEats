package com.example.rose.caloreats;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;
import android.view.View;
import android.content.Intent;

import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.text.Html;
import android.text.method.LinkMovementMethod;

public class NewFood extends AppCompatActivity {

    SQLiteDatabase restaurantDB;

    TextView titleT;
    TextView restaurantT;
    TextView websiteT;
    TextView caloriesT;
    TextView priceT;
    ImageView imageView;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_food);

        initDB();
        titleT = findViewById(R.id.title);
        restaurantT = findViewById(R.id.restaurant);
        websiteT = findViewById(R.id.website);
        caloriesT = findViewById(R.id.calories);
        priceT = findViewById(R.id.price);
        imageView = findViewById(R.id.food_image);
        saveButton = findViewById(R.id.save_button);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String cals = intent.getStringExtra("cals");
        String price = intent.getStringExtra("price");
        String res_id = intent.getStringExtra("res_id");

        titleT.setText(name);
        caloriesT.setText("Calories: " + cals);
        priceT.setText("Price: " + price);

        handleRestaurant(res_id);

        final Food food = new Food( name, cals, price);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood(food);
            }
        });

    }

    private void initDB(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            Log.e("DB", "Fail to create database");
        }
        restaurantDB = dbHelper.getReadableDatabase();
    }

    private void saveFood(Food food){
        //save to diary
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        food.setUserID(user.getUid());
        long millis=System.currentTimeMillis();
        java.sql.Date date= new java.sql.Date(millis);
        Firestore.getInstance().saveFood(food, date.toString());
        finish();
    }

    private void handleRestaurant(String res_id){
        String table = "restaurants";
        String columns = "restaurants._id as _id, name, rating, url, category_name";

        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();
        String queryString = "";

        where.add("(restaurants._id = ?)");
        args.add(res_id);

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
                System.out.println("Something's wrong :(");
            } else {
                c.moveToFirst();
                restaurantT.setText("Restaurant: " + c.getString(c.getColumnIndexOrThrow("name")));
                websiteT.setText(Html.fromHtml("<a href=\"" + c.getString(c.getColumnIndexOrThrow("url")) + "\">website</a>"));
                websiteT.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }
}
