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
        Restaurant res = intent.getParcelableExtra("restaurant");

        titleT.setText(name);
        restaurantT.setText(res.name);
        websiteT.setText(res.url);
        caloriesT.setText("Calories: " + cals);
        priceT.setText("Price: " + price);

        final Food food = new Food( name, cals, price);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood(food);
            }
        });

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

}
