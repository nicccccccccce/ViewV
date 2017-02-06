package com.sino.bridge.viewv;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.AnnotationTypeMismatchException;

/**
 * Created by user on 2016/12/6.
 */
public class ViewTest extends ViewGroup {

    private static String TAG = "ViewTest";

    public ViewTest(Context context) {
        super(context);
    }

    public ViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewTest(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        measureChildren(widthMeasureSpec, heightMeasureSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
//                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int cCount = getChildCount();
//        Log.e(TAG, "[countSize]" + cCount);
        MarginLayoutParams params = null;
        int sw = 0;
        int lineW = 0;
        int sh = 0;
        int th = 0;
        int lastH = 0;
        int lastW = 0;
        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            params = (MarginLayoutParams) childView.getLayoutParams();
            int w = childView.getMeasuredWidth() + childView.getPaddingRight() + childView.getPaddingLeft() + params.leftMargin + params.rightMargin;
            int h = childView.getMeasuredHeight() + childView.getPaddingBottom() + childView.getPaddingTop() + params.topMargin + params.bottomMargin;
            sh = Math.max(lastH, h);
            lastH = h;
            if (sw <= sizeWidth) {
                sw += w;
            }
            if (sw > sizeWidth) {
                lineW = Math.max(lastW,sw-w);//
                lastW = sw-w;
                th += sh;
                sw = 0;
            }


        }
        th = th != 0 ? th + sh : 0;
        setMeasuredDimension((widthMode == MeasureSpec.AT_MOST) ? Math.max(sw, lineW)
                : sizeWidth, (heightMode == MeasureSpec.AT_MOST) ? Math.max(sh, th)
                : sizeHeight);

    }

    // abstract method in viewgroup
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cl = l;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            r = cl + childView.getMeasuredWidth();
            b = t + childView.getMeasuredHeight();
            childView.layout(cl, t, r, b);
            cl = r + 10;
            if (cl + childView.getMeasuredWidth() > getWidth()) {
                cl = l;
                t = b + 10;
            }
        }

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

   /* @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewTest.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }*/
   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "v-dispatchTouchEvent"+super.dispatchTouchEvent(ev));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "v-onTouchEvent"+super.onTouchEvent(event));
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "v-onInterceptTouchEvent"+super.onInterceptTouchEvent(ev));
        return super.onInterceptTouchEvent(ev);
    }*/

}
