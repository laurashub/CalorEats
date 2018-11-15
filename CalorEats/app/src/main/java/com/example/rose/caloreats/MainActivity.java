package com.example.rose.caloreats;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;


import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ArrayList<ArrayList<Food>> diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Firestore firestore = Firestore.getInstance();
        firestore.init();

        ArrayList<String> dates = new ArrayList<>();
        diary = Firestore.getInstance().getDiary(dates);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this.getApplicationContext(), this, dates, diary));

    }


}
