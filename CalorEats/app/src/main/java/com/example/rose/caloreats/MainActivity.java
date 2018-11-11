package com.example.rose.caloreats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import android.util.Log;

import android.support.v4.view.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.support.annotation.NonNull;

import android.support.v4.app.FragmentTransaction;

import java.util.List;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        mAuth = FirebaseAuth.getInstance();
        int RQ_CODE = 123;

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), RQ_CODE);
        }

        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            Log.e("DB", "Fail to create database");
        }


        //initRestaurantDB();
    }
}
