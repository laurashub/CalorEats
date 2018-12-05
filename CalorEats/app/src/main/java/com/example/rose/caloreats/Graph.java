package com.example.rose.caloreats;

import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

//enum Type{ BAR, PIE }

//similar to project 4 tetris view extension
public class Graph extends View {

    Paint paint;
    ArrayList<ArrayList<Food>> diary;
    HashMap<String, Integer> totals;

    public Graph (Context context, AttributeSet attrs) {
        super(context, attrs);
        totals = new HashMap<>();
        paint = new Paint();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);


        float unit_width = (float) getWidth() / 15;
        float unit_height = (float) getHeight() / 2000;

        System.out.println("Width: " + unit_width + ", height: " + unit_height);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(0, 0,
                    getWidth(), getHeight(), paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < 10; i++){
                canvas.drawRect(0, (i*200)*unit_height,
                        getWidth(), ((i*200)+2)*unit_height, paint);
        }

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        ArrayList<String> dates = Firestore.getInstance().getDateArray();
        for (int i = 0; i < 7; i++) {
            canvas.drawRect(unit_width * (2 * i + 1),
                    getHeight() - unit_height * (totals.getOrDefault(dates.get(6-i), 0)),
                        unit_width * (2 * i + 2), getHeight(), paint);
        }
    }


    public void updateDiary(String date, ArrayList<Food> foods){

        int total = 0;

        for (Food f : foods){
            total += Integer.parseInt(f.getCals());
        }

        totals.put(date, total);
        this.invalidate();
    }

}
