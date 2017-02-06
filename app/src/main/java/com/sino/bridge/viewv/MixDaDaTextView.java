package com.sino.bridge.viewv;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2017/1/5.
 */

public class MixDaDaTextView extends View {
    public final static int LEFT = 0;
    public final static int TOP = 1;
    public final static int RIGHT = 2;
    public final static int BOTTOM = 3;
    private int id_left, id_top, id_right, id_bottom;
    private int rx;
    private int ry;
    private int ts;
    private int space;
    private String text;
    private int color;

    public MixDaDaTextView(Context context) {
        super(context);
    }

    public MixDaDaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.MixDaDaTextView);
        id_left = t.getResourceId(R.styleable.MixDaDaTextView_drawableLeft, -1);
        id_top = t.getResourceId(R.styleable.MixDaDaTextView_drawableTop, -1);
        id_right = t.getResourceId(R.styleable.MixDaDaTextView_drawableRight, -1);
        id_bottom = t.getResourceId(R.styleable.MixDaDaTextView_drawableBottom, -1);
        rx = t.getDimensionPixelSize(R.styleable.MixDaDaTextView_rx, 50);
        ry = t.getDimensionPixelSize(R.styleable.MixDaDaTextView_ry, 50);
        ts = t.getDimensionPixelSize(R.styleable.MixDaDaTextView_textMixSize, 40);
        space = t.getDimensionPixelSize(R.styleable.MixDaDaTextView_space, 10);
        text = t.getString(R.styleable.MixDaDaTextView_textMix);
        color = t.getColor(R.styleable.MixDaDaTextView_textMixColor, 0xFFAB47BC);
    }

    public MixDaDaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MixDaDaTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 确定中间一个文本四周为图片
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth(1);
        p.setTextSize(ts);
        p.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
        int baseline = y + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(text, x, baseline, p);
        if (id_top != -1) {
            //上
            Bitmap bitmap_t = getBitmap(id_top, rx * 2, ry * 2);
            RectF oval_t = new RectF(x - rx, y - ts / 2 - ry * 2 - space, x + rx, y - ts / 2 - space);
            canvas.drawBitmap(bitmap_t, oval_t.left, oval_t.top, p);
        }
        if (id_bottom != -1) {
            //下
            Bitmap bitmap_b = getBitmap(id_bottom, rx * 2, ry * 2);
            RectF oval_b = new RectF(x - rx, y + ts / 2 + space, x + rx, y + ts / 2 + ry * 2 + space);
            canvas.drawBitmap(bitmap_b, oval_b.left, oval_b.top, p);
        }
        if (id_left != -1) {
            //左
            Bitmap bitmap_l = getBitmap(id_left, rx * 2, ry * 2);
            RectF oval_l = new RectF(x - ts * text.length() / 2 - rx * 2 - space, y - ry, x - ts * text.length() / 2 - space, y + ry);
            canvas.drawBitmap(bitmap_l, oval_l.left, oval_l.top, p);
        }
        if (id_right != -1) {
            //右
            Bitmap bitmap_r = getBitmap(id_right, rx * 2, ry * 2);
            RectF oval_r = new RectF(x + ts * text.length() / 2 + space, y - ry, x + ts * text.length() / 2 + rx * 2 + space, y + ry);
            canvas.drawBitmap(bitmap_r, oval_r.left, oval_r.top, p);

        }

    }

    private Bitmap getBitmap(int resId, int dsW, int dsH) {
        Bitmap bitmap_r = BitmapFactory.decodeResource(getResources(), resId);
        Bitmap bitmap_sr = Bitmap.createScaledBitmap(bitmap_r, dsW, dsH, true);
        return bitmap_sr;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int m_w = MeasureSpec.getMode(widthMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int m_h = MeasureSpec.getMode(heightMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int ww = 0;
        int hh = 0;
        if (m_w == MeasureSpec.AT_MOST) {
            if (id_left != -1 || id_right != -1) {
                ww += (rx * 2 + space) * 2;
                ww += ts * text.length();
            }

        }

        if (m_h == MeasureSpec.AT_MOST) {
            if (id_top != -1 || id_bottom != -1) {
                hh += (ry * 2 + space) * 2;
                hh += ts;
            }


        }
        setMeasuredDimension(ww, hh);
    }

    private void dOO(Paint.FontMetricsInt fontMetrics, Canvas canvas, int x, int y, Paint p) {
        if (id_top != -1) {
            //上
            Bitmap bitmap_t = getBitmap(id_top, rx * 2, ry * 2);
            RectF oval_t = new RectF(x / 2 - rx, y, x / 2 + rx, y - ry * 2);
            canvas.drawBitmap(bitmap_t, oval_t.left, oval_t.top, p);
            int bt = y - ry * 2 - space + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            canvas.drawText(text, x, bt, p);
        }
        if (id_bottom != -1) {
            //下
            Bitmap bitmap_b = getBitmap(id_bottom, rx * 2, ry * 2);
            RectF oval_b = new RectF(x / 2 - rx, 0, x / 2 + rx, ry * 2);
            canvas.drawBitmap(bitmap_b, oval_b.left, oval_b.top, p);
            int bb = ry * 2 + space + ts + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            canvas.drawText(text, x, bb, p);
        }
        if (id_left != -1) {
            //左
            Bitmap bitmap_l = getBitmap(id_left, rx * 2, ry * 2);
            RectF oval_l = new RectF(x - rx * 2, y / 2 - ry, x, y / 2 + ry);
            canvas.drawRect(oval_l, p);
            canvas.drawBitmap(bitmap_l, oval_l.left, oval_l.top, p);
            int bf = y / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            canvas.drawText(text, x, bf, p);
        }
        if (id_right != -1) {
            //右
            Bitmap bitmap_r = getBitmap(id_right, rx * 2, ry * 2);
            RectF oval_r = new RectF(0, y / 2 - ry, rx * 2, y / 2 + ry);
            canvas.drawBitmap(bitmap_r, oval_r.left, oval_r.top, p);
            int bt = y / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            canvas.drawText(text, x, bt, p);
        }
    }
}
