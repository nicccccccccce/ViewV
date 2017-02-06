package com.sino.bridge.viewv;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by user on 2016/12/26.
 */
public class SufferViewDemo extends SurfaceView implements SurfaceHolder.Callback {
    private static String TAG = "SufferViewDemo";
    SurfaceHolder holder;
    private MyThread mThread;

    public SufferViewDemo(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (mThread == null)
            mThread = new MyThread(holder, w / 2, h / 2); //创建一个绘图线程
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public SufferViewDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SufferViewDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SufferViewDemo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated is called");
        mThread.isRun = true;
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged is called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed is called");
        mThread.isRun = false;
    }

    class MyThread extends Thread {
        private SurfaceHolder holder;
        public boolean isRun = false;
        int centerX;
        int centerY;

        public MyThread(SurfaceHolder holder, int x, int y) {
            this.holder = holder;
            isRun = true;
            this.centerX = x;
            this.centerY = y;
            Log.i(TAG, "MyThread set surface holder");
        }

        @Override
        public void run() {
            Canvas canvas = null;
            int count = 0;
            boolean isAdd = true;
            while (isRun) {
                try {

                    synchronized (holder) {
                        canvas = holder.lockCanvas();// 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                        canvas.drawColor(Color.BLACK);// 设置画布背景颜色
                        Paint p = new Paint(); // 创建画笔

                        if (isAdd)
                            ++count;
                        else
                            --count;
                        if (count == 500) {
                            isAdd = false;
                        }
                        if (count == 0) {
                            isAdd = true;
                        }
                        p.setColor(0xFF6A1B9A);
                        canvas.drawCircle(centerX, centerY, count + 100, p);
                        p.setColor(0xFF8E24AA);
                        canvas.drawCircle(centerX, centerY, count + 50, p);
                        p.setColor(0xFFAB47BC);
                        canvas.drawCircle(centerX, centerY, count, p);
                        p.setTextSize(count-20<0?1:count-20);
                        p.setColor(0xFFFFFFFF);
                        p.setTextAlign(Paint.Align.CENTER);
                        Rect targetRect = new Rect(centerX-count,centerY-count,centerX+count,centerY+count);
                        Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
                        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                        canvas.drawText("嗯哼",targetRect.centerX(), baseline,p);
//                        Log.i(TAG, "MyThread count" + count);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas);// 结束锁定画图，并提交改变。
                }
            }
        }
    }
}
