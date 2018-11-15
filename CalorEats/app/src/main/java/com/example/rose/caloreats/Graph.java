package com.example.rose.caloreats;

import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.lang.reflect.Array;
import java.util.ArrayList;

//enum Type{ BAR, PIE }

//similar to project 4 tetris view extension
public class Graph extends View {

    Paint paint;

    ArrayList<ArrayList<Food>> diary;
    int[] totals;
    int max;

    public Graph (Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if (diary != null) {

            calculateTotals();

            float unit_width = (float) getWidth() / 15;
            float unit_height = (float) getHeight() / 2000;

            System.out.println("Width: " + unit_width + ", height: " + unit_height);

            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawRect(0, 0,
                    getWidth(), getHeight(), paint);

            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);

            for (int i = 0; i < 7; i++) {
                System.out.println(totals[6 - i]);
                canvas.drawRect(unit_width * (2 * i + 1), unit_height * (totals[6 - i]),
                        unit_width * (2 * i + 2), 0, paint);
            }
        }
    }

    public void setDiary(ArrayList<ArrayList<Food>> diary_){
        diary = diary_;
        totals = new int[7];
        max = 0;
        calculateTotals();
    }

    private void calculateTotals(){
        for (int i = 0; i < 7; i++){
            totals[i] = 0;
            for (int j = 0; j < diary.get(i).size(); j++){

                Food f = diary.get(i).get(j);
                totals[i] += Integer.parseInt(f.getCals());

            }
            if (totals[i] > max) max = totals[i];
            System.out.println(i + ": " + totals[i]);
        }

    }

}
