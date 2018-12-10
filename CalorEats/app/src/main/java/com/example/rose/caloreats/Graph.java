package com.example.rose.caloreats;

import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

//similar to project 4 tetris view extension
public class Graph extends View {

    Paint paint;
    ArrayList<String> dates;
    HashMap<String, Integer> totals;
    int xIncrement = 17;
    int yIncrement;
    int limit;

    public Graph (Context context, AttributeSet attrs) {
        super(context, attrs);
        totals = new HashMap<>();
        paint = new Paint();
        Firestore.getInstance().getCalLimit(null, null, totals);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        this.dates = Firestore.getInstance().getDateArray();

        if (totals.keySet().size() == 8) {

            int max = Collections.max(totals.values());
            yIncrement = Math.max(((max + 99) / 100 ) * 100, totals.get("limit"));
            yIncrement += 200;

            //only draw if we have all the information that we need
            float unit_width = (float) getWidth() / xIncrement;
            float unit_height = (float) getHeight() / yIncrement;

            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawRect(0, 0,
                    getWidth(), getHeight(), paint);


            for (int i = 0; i < (yIncrement / 200); i++) {

                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawRect(0, (i * 200) * unit_height,
                        getWidth(), ((i * 200) + 2) * unit_height, paint);

                paint.setTextSize(30);
                canvas.drawText(Integer.toString(yIncrement - (i * 200)), 5, i * 200 * unit_height-2, paint);
            }

            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawRect(0, getHeight() - unit_height * totals.get("limit")-(2*unit_height),
                    getWidth(), (getHeight() - unit_height * totals.get("limit")) + (2*unit_height) , paint);

            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);

            for (int i = 0; i < 7; i++) {
                canvas.drawRect(unit_width * ((2*(i+1))+1),
                        getHeight() - unit_height * (totals.getOrDefault(dates.get(6 - i), 0)),
                        unit_width * ((2*(i+1))+2), getHeight(), paint);
            }
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

    public void setLimit(int limit){
        this.limit = limit;
    }

}
