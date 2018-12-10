package com.example.rose.caloreats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    EditText cal_limit;
    Button set;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.user_settings);
        Intent intent = getIntent();
        cal_limit = (EditText) findViewById(R.id.cal_limit_edit);
        set = (Button) findViewById(R.id.cal_limit_new);
        Firestore.getInstance().getCalLimit(cal_limit, null, null);
    }

    public void setClicked(View view){
        int new_limit = Integer.parseInt(cal_limit.getText().toString());
        Firestore.getInstance().setCalLimit(getApplicationContext(), new_limit);
        finish();
    }


}