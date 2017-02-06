package com.sino.bridge.viewv;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 2017/1/24.
 */

public class PieChat extends View {
    float x = 0;
    float y = 0;
    int angel = 0;
    RectF oval;

    public PieChat(Context context) {
        super(context);
    }

    public PieChat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PieChat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        oval = new RectF(centerX - 200, centerY - 200, centerX + 200, centerY + 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(angel, oval.centerX(), oval.centerY());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(0xFF6A1B9A);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(1);
        p.setTextSize(20);
        p.setColor(0xFFBA68C8);
        canvas.drawArc(oval, 0 + 180, 120, true, p);
        p.setColor(0xFF9C27B0);
        canvas.drawArc(oval, 120 + 180, 120, true, p);
        p.setColor(0xFF4A148C);
        canvas.drawArc(oval, 240 + 180, 120, true, p);
        canvas.save();
        canvas.restoreToCount(0);
        p.setColor(0xFFD32F2F);
        canvas.drawCircle(oval.centerX(), oval.centerY(), 100, p);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(0xFFFFFFFF);
        Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
        float baseline = oval.centerY() + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText("开始", oval.centerX(), baseline, p);
        RectF rf = new RectF(oval.left, oval.bottom, oval.right, oval.bottom + 60);
        canvas.drawRect(rf, p);
        p.setColor(0xFF909090);
        float b = rf.centerY() + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText("开始", rf.centerX(), b, p);
        /*p.setStyle(Paint.Style.STROKE);
        Path mPath = new Path();
        float r = (oval.top - oval.bottom) / 2;
        float ty = (float) (Math.sin(Math.toRadians(60)) * r) + oval.centerY();
        float tx = (float) (Math.cos(Math.toRadians(60)) * r) + oval.centerX();
        mPath.moveTo(tx, ty);
        mPath.lineTo(tx + 30, ty + 30);
        mPath.lineTo(tx + 30 + 60, ty + 30);
        p.setColor(0xFFFFFFFF);
        canvas.drawPath(mPath, p);
        drawAL(canvas, p, (int) tx + 30, (int) ty + 30, (int) tx + 30 + 60, (int) ty + 30);*/
        canvas.save();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getX();
        float rawY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                rawX = event.getRawX();
                rawY = event.getRawY();
                Log.e("TAGGG", (rawX - x) + "" + (rawY - y));
                if (rawX < oval.left || rawX > oval.right || rawY < oval.top || rawY > oval.bottom)
                    return false;
                /*if (rawX - x < 1 || rawX - x > -1 || rawY - y < 1 || rawY - y > -1)
                    break;*/
                if (rawX - x > 0 && rawY < oval.centerY())
                    angel++;
                else if (rawX - x < 0 && rawY < oval.centerY())
                    angel--;
                else if (rawY - y > 0 && rawX > oval.centerX())
                    angel++;
                else if (rawY - y < 0 && rawX > oval.centerX())
                    angel--;
                else if (rawX - x > 0 && rawY > oval.centerY())
                    angel--;
                else if (rawX - x < 0 && rawY > oval.centerY())
                    angel++;
                else if (rawY - y > 0 && rawX < oval.centerX())
                    angel--;
                else if (rawY - y < 0 && rawX < oval.centerX())
                    angel++;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (x == rawX && y == rawY && (rawX > oval.centerX() - 100 && rawX < oval.centerX() + 100)
                        && (rawY > oval.centerY() - 100 && rawX < oval.centerY() + 100)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i <= 360 * 2; i++) {
                                angel++;
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                postInvalidate();
                            }
                        }
                    }).start();
                }
                x = rawX;
                y = rawY;
                break;
        }
        return true;
    }

    public void drawAL(Canvas canvas, Paint myPaint, int sx, int sy, int ex, int ey) {
        double H = 8; // 箭头高度
        double L = 3.5; // 底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
        double y_4 = ey - arrXY_2[1];
        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        // 画线
        canvas.drawLine(sx, sy, ex, ey, myPaint);
        Path triangle = new Path();
        triangle.moveTo(ex, ey);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.close();
        canvas.drawPath(triangle, myPaint);

    }

    // 计算
    public double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {
        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }
}
