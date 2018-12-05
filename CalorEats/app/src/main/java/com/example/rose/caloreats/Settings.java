package com.example.rose.caloreats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    EditText cal_limit;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.user_settings);
        Intent intent = getIntent();
        cal_limit = (EditText) findViewById(R.id.cal_limit_edit);
        Firestore.getInstance().getCalLimit(cal_limit);
    }

    public void cancelClicked(View view){
        finish();
    }

    public void okClicked(View view){
        finish(); // calls onDestroy
    }




}