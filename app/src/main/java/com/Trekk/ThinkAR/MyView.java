package com.Trekk.ThinkAR;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by timgrable1 on 9/21/16.
 */

public class MyView extends View {

    private int circleAngle;

    private Paint bottomPaint;
    private Paint middlePaint;
    private Paint topPaint;
    private Paint textPaint;

    //circle and text colors
    private int circleCol, labelCol;
    //label text
    private String circleText;
    //paint for drawing custom view


    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyView, 0, 0);
        int myColor = context.getResources().getColor(R.color.colorPrimary);

        bottomPaint = new Paint();
        bottomPaint.setColor(Color.LTGRAY);
        bottomPaint.setStrokeWidth(10);
        bottomPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        middlePaint = new Paint();
        middlePaint.setColor(myColor);
        middlePaint.setStrokeWidth(10);
        middlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //paint object for drawing in onDraw
        topPaint = new Paint();
        topPaint.setColor(Color.WHITE);
        topPaint.setStrokeWidth(10);
        topPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //paint object for drawing in onDraw
        textPaint = new Paint();
        textPaint.setColor(myColor);
        textPaint.setStrokeWidth(1);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        try {
            //get the text and colors specified using the names in attrs.xml
            circleText = a.getString(R.styleable.MyView_circleLabel);
            circleCol = a.getInteger(R.styleable.MyView_circleColor, 0); //0 is default
            labelCol = a.getInteger(R.styleable.MyView_labelColor, 0);
        } finally {
            a.recycle();
        }
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //get half of the width and height as we are working with a circle
        int viewHeight = this.getMeasuredHeight() - 25;

         /**  Bottom Circle used as the gray background **/
        final RectF bottomCircle = new RectF();
        bottomPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bottomPaint.setAntiAlias(true);
        bottomCircle.set(25, 25, viewHeight - 25, viewHeight - 25);
        canvas.drawArc(bottomCircle, 270, 360, true, bottomPaint);

        /**  Middle circle based on the number of markers scanned **/
        final RectF oval = new RectF();
        middlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        middlePaint.setAntiAlias(true);
        oval.set(25, 25, viewHeight - 25, viewHeight - 25);
        canvas.drawArc(oval, 270, circleAngle, true, middlePaint);

        /** Top white circle **/
        final RectF topCircle = new RectF();
        topPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        topPaint.setAntiAlias(true);
        topCircle.set(100, 100, viewHeight - 100, viewHeight - 100);
        canvas.drawArc(topCircle, 270, 360, true, topPaint);

        /** Text overlay displaying the number of markers scanned **/
        textPaint.setColor(labelCol);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(76);

        //draw the text using the string attribute and chosen properties
        canvas.drawText(circleText, oval.centerX(), oval.centerY() + 25, textPaint);
    }

    public int getCircleAngle(){
        return circleAngle;
    }

    public void setCircleAngle(int newAngle){
        //update the instance variable
        circleAngle = newAngle;

        //redraw the view
        invalidate();
        requestLayout();
    }

    public void setCircleText(String newLabel){
        //update the instance variable
        circleText = newLabel;

        //redraw the view
        invalidate();
        requestLayout();
    }
}
