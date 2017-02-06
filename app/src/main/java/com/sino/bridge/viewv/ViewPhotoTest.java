package com.sino.bridge.viewv;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

/**
 * Created by user on 2016/12/23.
 */
public class ViewPhotoTest extends ViewGroup {
    public ViewPhotoTest(Context context) {
        super(context);
    }

    public ViewPhotoTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPhotoTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPhotoTest(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mViewGroupWidth  = getMeasuredWidth();  //当前ViewGroup的总宽度
        int mViewGroupHeight = getMeasuredHeight(); //当前ViewGroup的总高度
        int mPainterPosX = l; //当前绘图光标横坐标位置
        int mPainterPosY = t;  //当前绘图光标纵坐标位置

        int childCount = getChildCount();
        for ( int i = 0; i < childCount; i++ ) {

            View childView = getChildAt(i);

            int width  = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();

            ViewPhotoTest.MarginLayoutParams margins = (MarginLayoutParams) childView.getLayoutParams();

            //ChildView占用的width  = width+leftMargin+rightMargin
            //ChildView占用的height = height+topMargin+bottomMargin
            //如果剩余的空间不够，则移到下一行开始位置
            if( mPainterPosX + width + margins.leftMargin + margins.rightMargin > mViewGroupWidth ) {
                mPainterPosX = l;
                mPainterPosY += height + margins.topMargin + margins.bottomMargin;
            }

            //执行ChildView的绘制
            childView.layout(mPainterPosX+margins.leftMargin, mPainterPosY+margins.topMargin,mPainterPosX+margins.leftMargin+width, mPainterPosY+margins.topMargin+height);

            mPainterPosX += width + margins.leftMargin + margins.rightMargin;
        }
    }
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return super.generateLayoutParams(attrs);
       return  new ViewPhotoTest.MarginLayoutParams(getContext(),attrs);
    }

    public static class MarginLayoutParams extends ViewGroup.LayoutParams {
        public int leftMargin;
        public int topMargin;
        public int rightMargin;
        public int bottomMargin;

        public MarginLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ViewPhotoTest);
            leftMargin = a.getDimensionPixelSize(R.styleable.ViewPhotoTest_leftMargin, 0);
            topMargin = a.getDimensionPixelSize(R.styleable.ViewPhotoTest_topMargin, 0);
            rightMargin = a.getDimensionPixelSize(R.styleable.ViewPhotoTest_rightMargin, 0);
            bottomMargin = a.getDimensionPixelSize(R.styleable.ViewPhotoTest_bottomMargin, 0);
            a.recycle();
        }

        public MarginLayoutParams(int width, int height) {
            super(width, height);
        }
        public MarginLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
