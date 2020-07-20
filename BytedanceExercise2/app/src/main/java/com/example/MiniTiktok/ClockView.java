package com.example.MiniTiktok;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClockView extends View {
    private static final int FULL_CIRCLE_DEGREE = 360;
    private static final int UNIT_DEGREE = 6;

    private static final float UNIT_LINE_WIDTH = 8; // 刻度线的宽度
    private static final int HIGHLIGHT_UNIT_ALPHA = 0xFF;
    private static final int NORMAL_UNIT_ALPHA = 0x80;

    private static final float HOUR_NEEDLE_LENGTH_RATIO = 0.4f; // 时针长度相对表盘半径的比例
    private static final float MINUTE_NEEDLE_LENGTH_RATIO = 0.6f; // 分针长度相对表盘半径的比例
    private static final float SECOND_NEEDLE_LENGTH_RATIO = 0.8f; // 秒针长度相对表盘半径的比例
    private static final float HOUR_NEEDLE_WIDTH = 12; // 时针的宽度
    private static final float MINUTE_NEEDLE_WIDTH = 8; // 分针的宽度
    private static final float SECOND_NEEDLE_WIDTH = 4; // 秒针的宽度
    private static final float TIME_TEXT_SIZE = 40;    //数字大小

    private Calendar calendar = Calendar.getInstance();

    private float radius = 0; // 表盘半径
    private float centerX = 0; // 表盘圆心X坐标
    private float centerY = 0; // 表盘圆心Y坐标

    private String[] number = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "1", "2"};

    private List<RectF> unitLinePositions = new ArrayList<>();
    private Paint unitPaint = new Paint();
    private Paint hourNeedlePaint = new Paint();
    private Paint minuteNeedlePaint = new Paint();
    private Paint secondNeedlePaint = new Paint();
    private Paint numberPaint = new Paint();

    private Handler handler = new Handler();
    private MyInterface onTimeChangedListener;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        unitPaint.setAntiAlias(true);
        unitPaint.setColor(Color.WHITE);
        unitPaint.setStrokeWidth(UNIT_LINE_WIDTH);
        unitPaint.setStrokeCap(Paint.Cap.ROUND);
        unitPaint.setStyle(Paint.Style.STROKE);

        // TODO 设置绘制时、分、秒针的画笔: needlePaint
        hourNeedlePaint.setAntiAlias(true);
        hourNeedlePaint.setColor(Color.WHITE);
        hourNeedlePaint.setStrokeWidth(HOUR_NEEDLE_WIDTH);
        hourNeedlePaint.setStrokeCap(Paint.Cap.ROUND);
        hourNeedlePaint.setStyle(Paint.Style.STROKE);

        minuteNeedlePaint.setAntiAlias(true);
        minuteNeedlePaint.setColor(Color.WHITE);
        minuteNeedlePaint.setStrokeWidth(MINUTE_NEEDLE_WIDTH);
        minuteNeedlePaint.setStrokeCap(Paint.Cap.ROUND);
        minuteNeedlePaint.setStyle(Paint.Style.STROKE);

        secondNeedlePaint.setAntiAlias(true);
        secondNeedlePaint.setColor(Color.WHITE);
        secondNeedlePaint.setStrokeWidth(SECOND_NEEDLE_WIDTH);
        secondNeedlePaint.setStrokeCap(Paint.Cap.ROUND);
        secondNeedlePaint.setStyle(Paint.Style.STROKE);


        numberPaint.setAntiAlias(true);
        numberPaint.setDither(true);
        numberPaint.setTextSize(TIME_TEXT_SIZE);
        numberPaint.setColor(Color.WHITE);
        new Thread() {
            public void run() {
                while (true) {
                    postInvalidate();//刷新view
                    try {
                        Thread.sleep(1000);//每秒循环一次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        configWhenLayoutChanged();
    }

    private void configWhenLayoutChanged() {
        float newRadius = Math.min(getWidth(), getHeight()) / 2f;
        if (newRadius == radius) {
            return;
        }
        radius = newRadius;
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;

        // 当视图的宽高确定后就可以提前计算表盘的刻度线的起止坐标了
        for (int degree = 0; degree < FULL_CIRCLE_DEGREE; degree += UNIT_DEGREE) {
            double radians = Math.toRadians(degree);
            float startX = (float) (centerX + (radius * (1 - 0.05f)) * Math.cos(radians));
            float startY = (float) (centerY + (radius * (1 - 0.05f)) * Math.sin(radians));
            float stopX = (float) (centerX + radius * Math.cos(radians));
            float stopY = (float) (centerY + radius * Math.sin(radians));
            unitLinePositions.add(new RectF(startX, startY, stopX, stopY));
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        drawUnit(canvas);
        drawTimeNumbers(canvas);
        drawTimeNeedles(canvas);
    }

    // 绘制表盘上的刻度
    private void drawUnit(Canvas canvas) {
        for (int i = 0; i < unitLinePositions.size(); i++) {
            if (i % 5 == 0) {
                unitPaint.setAlpha(HIGHLIGHT_UNIT_ALPHA);
            } else {
                unitPaint.setAlpha(NORMAL_UNIT_ALPHA);
            }
            RectF linePosition = unitLinePositions.get(i);
            //canvas.drawLine(linePosition.left, linePosition.top, linePosition.right, linePosition.bottom, unitPaint);
            canvas.drawPoint(linePosition.right,linePosition.bottom,unitPaint);
        }
    }

    private void drawTimeNeedles(Canvas canvas) {
        Time time = getCurrentTime();
        onTimeChanged(onTimeChangedListener,time);
        int hour = time.getHours();
        int minute = time.getMinutes();
        int second = time.getSeconds();
        double hourRadian = Math.toRadians((hour-3)*30);
        double minuteRadian = Math.toRadians((minute-15)*6);
        double secondRadian = Math.toRadians((second-15)*6);

        float hourNeedleLength = radius * HOUR_NEEDLE_LENGTH_RATIO;
        float minuteNeedleLength = radius * MINUTE_NEEDLE_LENGTH_RATIO;
        float secondNeedleLength = radius * SECOND_NEEDLE_LENGTH_RATIO;

        float hourX = centerX + (float)(hourNeedleLength *  Math.cos(hourRadian));
        float hourY = centerY + (float)(hourNeedleLength *  Math.sin(hourRadian));
        float minuteX = centerX + (float)(minuteNeedleLength *  Math.cos(minuteRadian));
        float minuteY = centerY + (float)(minuteNeedleLength *  Math.sin(minuteRadian));
        float secondX = centerX + (float)(secondNeedleLength *  Math.cos(secondRadian));
        float secondY = centerY + (float)(secondNeedleLength *  Math.sin(secondRadian));

        canvas.drawLine(centerX,centerY,hourX,hourY,hourNeedlePaint);
        canvas.drawLine(centerX,centerY,minuteX,minuteY,minuteNeedlePaint);
        canvas.drawLine(centerX,centerY,secondX,secondY,secondNeedlePaint);

        /**
         * 思路：
         * 1、以时针为例，计算从0点（12点）到当前时间，时针需要转动的角度
         * 2、根据转动角度、时针长度和圆心坐标计算出时针终点坐标（起始点为圆心）
         * 3、从圆心到终点画一条线，此为时针
         * 注1：计算时针转动角度时要把时和分都得考虑进去
         * 注2：计算坐标时需要用到正余弦计算，请用Math.sin()和Math.cos()方法
         * 注3：Math.sin()和Math.cos()方法计算时使用不是角度而是弧度，所以需要先把角度转换成弧度，
         *     可以使用Math.toRadians()方法转换，例如Math.toRadians(180) = 3.1415926...(PI)
         * 注4：Android视图坐标系的0度方向是从圆心指向表盘3点方向，指向表盘的0点时是-90度或270度方向，要注意角度的转换
         */
    }

    private void drawTimeNumbers(Canvas canvas) {
        float distance = (1-0.1f)*radius;
        // 数字的坐标(a,b)
        float textX, textY;
        for (int i = 0; i < 12; i++) {
            double rad = 2 * Math.PI / 12 * i;
            textX = centerX + (float) (Math.cos(rad) * distance)-0.5f*TIME_TEXT_SIZE;
            textY = centerY + (float) (Math.sin(rad) * distance)+0.5f*TIME_TEXT_SIZE;
            canvas.drawText(number[i], textX, textY, numberPaint);
        }
    }

    public void setOnTimeChangeListener(MyInterface listener){
        onTimeChangedListener=listener;
    }
    private static void onTimeChanged(MyInterface timer,Time time){
        Bundle bundle = new Bundle();
        bundle.putInt("hour",time.getHours());
        bundle.putInt("minute",time.getMinutes());
        bundle.putInt("second",time.getSeconds());
        timer.onTimeChanged(bundle);
    }

    // 获取当前的时间：时、分、秒
    private Time getCurrentTime() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        return new Time(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }
    class Time{
        private int hours;
        private int minutes;
        private int seconds;

        Time(int hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public int getHours() {
            return hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public int getSeconds() {
            return seconds;
        }
    }

}