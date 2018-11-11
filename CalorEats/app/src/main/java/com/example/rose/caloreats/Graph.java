package com.example.rose.caloreats;

import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;

enum Type{ BAR, PIE }

//similar to project 4 tetris view extension
public class Graph extends View {

    public Graph (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);
    }

}
