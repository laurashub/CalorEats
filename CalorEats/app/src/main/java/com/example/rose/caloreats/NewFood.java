package com.example.rose.caloreats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.Random;
import android.view.View;
import android.content.Intent;

import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewFood extends AppCompatActivity {

    Random r;

    TextView title;
    ImageView imageView;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_food);
        r = new Random();

        title = findViewById(R.id.title);
        imageView = findViewById(R.id.food_image);
        saveButton = findViewById(R.id.save_button);

        //public Food(String name_, int cals_, int price_, String restaurant_, String address_){
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        title.setText(name);
        final Food food = new Food( name, intent.getStringExtra("cals"),
                intent.getStringExtra("price"));

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
