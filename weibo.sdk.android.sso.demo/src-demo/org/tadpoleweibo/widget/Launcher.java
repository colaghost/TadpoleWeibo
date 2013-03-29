package org.tadpoleweibo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class Launcher extends ViewGroup {

    static final String TAG = "Launcher";
    static final int ROW_COUNT = 4;

    private int mNumColums = 2;
    private FeatureMetrics mFeatureMetrics = new FeatureMetrics();

    public Launcher(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Launcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Launcher(Context context) {
        super(context);
        init();
    }



    private void init() {
        for (int i = 0, len = 8; i < len; i++) {
            TextView tv = new TextView(getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("terst1" + i);
            tv.setBackgroundColor(Color.GRAY);
            this.addView(tv);
        }

    }

    private int getRowSpacing() {
        return 20;
    }

    private int getColumnSpacing() {
        return 20;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout");
        this.debug(2);

        mFeatureMetrics.calculate(this);


        final int x = l + mFeatureMetrics.pLeft;
        final int y = t + mFeatureMetrics.pTop;
        final int childW = mFeatureMetrics.childW;
        final int childH = mFeatureMetrics.childH;
        final int cSpace = mFeatureMetrics.cSpace;
        final int rSpace = mFeatureMetrics.rSpace;
        final int numColums = mNumColums;


        int v = 0;
        int z = 0;
        int xTmp, yTmp;

        int childCount = getChildCount();
        Log.d(TAG, "childCount = " + childCount);

        View childView = null;
        for (int i = 0; i < childCount; i++) {
            childView = getChildAt(i);

            v = i / numColums;  // 第几行
            z = i % numColums;  // 第几列

            xTmp = x + z * childW + z * cSpace;
            yTmp = y + v * childH + v * rSpace;
            childView.layout(xTmp, yTmp, xTmp + childW, yTmp + childH);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }



    private int[] getPositionRect(int position) {
        final int x = mFeatureMetrics.pLeft;
        final int y = mFeatureMetrics.pTop;
        final int childW = mFeatureMetrics.childW;
        final int childH = mFeatureMetrics.childH;
        final int cSpace = mFeatureMetrics.cSpace;
        final int rSpace = mFeatureMetrics.rSpace;
        final int numColums = mNumColums;

        int v = position / numColums;  // 第几行
        int z = position % numColums;  // 第几列

        int xTmp = x + z * childW + z * cSpace;
        int yTmp = y + v * childH + v * rSpace;

        return new int[] { xTmp, yTmp };
    }

    public void moveNext(int pos) {
        View view = getChildAt(pos);
        Animation ani = getTranslateAnimation(pos, pos + 1);
        view.startAnimation(ani);
    }

    private Animation getTranslateAnimation(int fromPos, int toPos) {
        int[] fLocation = getPositionRect(fromPos);
        int[] tLocation = getPositionRect(toPos);
        TranslateAnimation transAni = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, tLocation[0] - fLocation[0], Animation.ABSOLUTE, 0, Animation.ABSOLUTE, tLocation[1]
                - fLocation[1]);
        transAni.setDuration(2000);
        return transAni;
    }

    /**
     * 属性值计算缓存
     * 
     * <br>==========================
     * <br> author：Zenip
     * <br> email：lxyczh@gmail.com
     * <br> create：2013-3-29
     * <br>==========================
     */
    private static class FeatureMetrics {
        boolean isCaculate;

        void calculate(Launcher view) {
            if (isCaculate == true) {
                return;
            }

            int measureWidth = view.getMeasuredWidth();
            int measureHeight = view.getMeasuredHeight();

            pLeft = view.getPaddingLeft();
            pRight = view.getPaddingRight();
            pTop = view.getPaddingTop();
            pBottom = view.getPaddingBottom();

            rSpace = view.getRowSpacing();
            cSpace = view.getColumnSpacing();

            childW = (measureWidth - pLeft - pRight - ((view.mNumColums - 1) * cSpace)) / view.mNumColums;
            childH = (measureHeight - pTop - pBottom - (ROW_COUNT - 1) * rSpace) / ROW_COUNT;

            isCaculate = true;
        }

        int pLeft, pRight, pTop, pBottom, rSpace, cSpace, childW, childH;
    }
}
