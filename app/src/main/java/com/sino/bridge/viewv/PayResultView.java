package com.sino.bridge.viewv;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2017/1/19.
 */

public class PayResultView extends View {
    public PayResultView(Context context) {
        super(context);
    }

    public PayResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PayResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PayResultView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(0xFF02b387);
        paint.setTextAlign(Paint.Align.CENTER);
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        int radius=30;
        int line_h=90;
        canvas.drawCircle(x, y-line_h-radius, radius, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40f);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = ((y-line_h-radius)*2 - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText("√", x, baseline, paint);
        paint.setColor(0xFF02b387);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        canvas.drawLine(x, y-line_h, x, y, paint);


        paint.setColor(0xFFb6b6b6);
        canvas.drawLine(x, y, x, y+line_h, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y+line_h+radius, radius, paint);
        paint.setColor(Color.WHITE);
        int baseline_g = ((y+line_h+radius)*2 - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText("￥",x, baseline_g, paint);
        canvas.save();
    }
}
