package com.sino.bridge.viewv;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * 只支持布局中使用,控制进度仿照progress setprogress
 * Created by user on 2017/1/3.
 */
public class RoundProgressView extends SurfaceView implements SurfaceHolder.Callback {
    private static String TAG = RoundProgressView.class.getSimpleName();
    SurfaceHolder holder;
    private MyThread mThread;
    private int radius;
    private int backGroundColor;
    private int roundBgColor;
    private int strokeWidth;
    private int strokeColor;
    private int textSize;
    private int textColor;

    public RoundProgressView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    public RoundProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressView);
        radius = typedArray.getInt(R.styleable.RoundProgressView_radius, 30);
        backGroundColor = typedArray.getInt(R.styleable.RoundProgressView_backGroundColor, 0xFFFFFFFF);
        roundBgColor = typedArray.getInt(R.styleable.RoundProgressView_roundBgColor, 0xFFAB47BC);
        strokeWidth = typedArray.getInt(R.styleable.RoundProgressView_strokeWidth, 5);
        strokeColor = typedArray.getInt(R.styleable.RoundProgressView_strokeColor, 0xFF00FFFF);
        textColor = typedArray.getInt(R.styleable.RoundProgressView_textColor, 0xFFFFFFFF);
        textSize = typedArray.getInt(R.styleable.RoundProgressView_textSize, 15);
        mThread = new MyThread(holder, true, radius, radius, radius, backGroundColor, roundBgColor, strokeWidth, strokeColor, textSize, textColor); //创建一个绘图线程
    }

    public RoundProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        holder = getHolder();
        holder.addCallback(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        获取宽度的模式和尺寸
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        //获取高度的模式和尺寸
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        //宽确定，高不确定
//        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST && radius != 0) {
//            widthMeasureSpec = MeasureSpec.makeMeasureSpec(radius * 2 + 10, MeasureSpec.EXACTLY);
//            heightMeasureSpec = widthMeasureSpec;
//            setMeasuredDimension(radius * 2 + 10,radius * 2 + 10);
//        } else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
        setMeasuredDimension(radius * 2 + strokeWidth, radius * 2 + strokeWidth);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated is called");
        mThread.isRun = true;
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed is called");
        mThread.isRun = false;
    }

    public void setRadio(int radio) {
        // 0~100
        if(radio*3.6>360){
            mThread.isRun = false;
            return;
        }
        mThread.setRadio((int) (radio*3.6));
    }

    class MyThread extends Thread {
        private SurfaceHolder holder;
        public boolean isRun = false;
        private int centerX;
        private int centerY;
        private int radius;
        private int backGroundColor;
        private int strokeWidth;
        private int strokeColor;
        private int textSize;
        private int textColor;
        private int roundColor;
        private int currentRadio = -1;
        private int radio;


        public void setRadio(int radio) {
            this.radio = radio;
        }

        public MyThread(SurfaceHolder holder, boolean isRun, int centerX, int centerY, int radius, int backGroundColor, int roundColor, int strokeWidth, int strokeColor, int textSize, int textColor) {
            this.holder = holder;
            this.isRun = isRun;
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.backGroundColor = backGroundColor;
            this.roundColor = roundColor;
            this.strokeWidth = strokeWidth;
            this.strokeColor = strokeColor;
            this.textSize = textSize;
            this.textColor = textColor;
        }

        @Override
        public void run() {
            Canvas canvas = null;
                while (isRun) {
                    if (currentRadio != radius)
                    try {
                        synchronized (holder) {
                            canvas = holder.lockCanvas();// 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                            canvas.drawColor(backGroundColor);// 设置画布背景颜色
                            Paint p = new Paint(); // 创建画笔
                            p.setColor(roundColor);
                            p.setAntiAlias(true);
                            canvas.drawCircle(centerX + strokeWidth / 2, centerY + strokeWidth / 2, radius, p);
                            p.setColor(strokeColor);
                            p.setStyle(Paint.Style.STROKE);
                            p.setStrokeWidth(strokeWidth);
                            // 实例化路径
                            Path mPath = new Path();
                            // 移动起点至0,0
                            mPath.moveTo(centerX - radius, centerY + strokeWidth);
                            // 连接路径到点
                            RectF oval = new RectF(centerX + strokeWidth / 2 - radius, centerY + strokeWidth / 2 - radius, centerY + strokeWidth / 2 + radius, centerY + strokeWidth / 2 + radius);
                            mPath.arcTo(oval, 0, radio, true);
                            currentRadio = radio;
                            Log.d(TAG, "[progress]" + radio);
                            canvas.drawPath(mPath, p);

//                        p.setColor(0xFFAB47BC);
                            p.setStrokeWidth(1);
                            p.setTextSize(textSize);
                            p.setColor(textColor);
                            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
                            p.setTextAlign(Paint.Align.CENTER);
                            Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
                            // 转载请注明出处：http://blog.csdn.net/hursing
                            float baseline = (oval.bottom + oval.top - fontMetrics.bottom - fontMetrics.top) / 2;
                            canvas.drawText("嗯哼", oval.centerX(), baseline, p);
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
