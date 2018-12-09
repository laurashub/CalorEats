package com.example.rose.caloreats;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ArrayList<ArrayList<Food>> diary;
    int RQ_CODE = 123;
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), RQ_CODE);
        } else {
            start();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQ_CODE) {
            if (resultCode == RESULT_OK) {
                start();
            }
        }
    }

    public void start() {
        Firestore firestore = Firestore.getInstance();
        firestore.init();

        Storage storage = Storage.getInstance();
        storage.init(getApplicationContext());

        UserData data = new UserData();

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), data);
        vpPager.setAdapter(adapterViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.calorie_limit:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
