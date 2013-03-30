package org.tadpoleweibo.widget;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.tadpoleweibo.common.TLog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Launcher extends ViewGroup {

    static final String TAG = "Launcher";
    static final int ROW_COUNT = 4;

    private int mNumColums = 2;

    private int dragPosition = INVALID_POSITION;
    private int dropPosition = INVALID_POSITION;

    private FeatureMetrics mFeatureMetrics = new FeatureMetrics();
    private WindowManager mWindowManager;


    private int array[] = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };

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

    private int getRowSpacing() {
        return 20;
    }

    private int getColumnSpacing() {
        return 20;
    }


    private void init() {
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        for (int i = 0, len = 8; i < len; i++) {
            LaucherItemView tv = new LaucherItemView(getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setText("terst1" + i);
            tv.setBackgroundColor(Color.GRAY);
            this.addView(tv);

            final int index = i;
            tv.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    startDrag(v, index);
                    return false;
                }
            });

        }
    }


    private ImageView mDragView;
    private View mHideView;

    private int lastX = 0;
    private int lastY = 0;

    static final int INVALID_POSITION = -1;

    private WindowManager.LayoutParams mDragWinLP;

    private int getHitPosition(int x, int y) {
        Rect rect = new Rect();
        int location[] = null;
        for (int i = 0; i < 8; i++) {
            location = getPositionRect(i);
            rect.set(location[0], location[1], location[0] + mFeatureMetrics.childW, location[1] + mFeatureMetrics.childH);
            if (rect.contains(x, y)) {
                return i;
            }
        }
        return INVALID_POSITION;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            lastX = (int) ev.getRawX();
            lastY = (int) ev.getRawY();
        }

        if (MotionEvent.ACTION_MOVE == ev.getAction()) {
            if (mDragView != null) {

                float xTemp = ev.getRawX();
                float yTmep = ev.getRawY();

                mDragWinLP.x += xTemp - lastX;
                mDragWinLP.y += yTmep - lastY;

                lastX = (int) xTemp;
                lastY = (int) yTmep;

                mWindowManager.updateViewLayout(mDragView, mDragWinLP);


                dragging(lastX, lastY);

            }
        }

        if (MotionEvent.ACTION_CANCEL == ev.getAction() || MotionEvent.ACTION_UP == ev.getAction()) {
            endDrag();

        }
        return super.dispatchTouchEvent(ev);
    }


    private void dragging(int x, int y) {
        int hitPosition = getHitPosition(x, y);

        final int[] arr = array;
        int realDragPostion = arr[dragPosition];

        if (hitPosition == realDragPostion || hitPosition == INVALID_POSITION) {
            return;
        }

        TLog.debug("", "hitPosition = %d, = %d", hitPosition, realDragPostion);

        HashMap<Integer, Integer> posToViewPos = getRealArrayHashMap();

        // move next
        if (hitPosition < realDragPostion) {
            for (int i = hitPosition; i < realDragPostion; i++) {
                moveNext(posToViewPos.get(i), i);
                arr[posToViewPos.get(i)] += 1;
            }
        }
        // move prev
        else {
            for (int i = hitPosition; i > realDragPostion; i--) {
                movePrev(posToViewPos.get(i), i);
                arr[posToViewPos.get(i)] -= 1;
            }
        }
        arr[dragPosition] = hitPosition;

        System.out.println("arr = " + Arrays.toString(arr));
    }

    public HashMap<Integer, Integer> getRealArrayHashMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < array.length; i++) {
            hashMap.put(array[i], i);
        }
        return hashMap;
    }

    private void startDrag(View v, int position) {
        final int statusBarHeight = getStatusHeight((Activity) getContext());

        mHideView = v;
        v.setVisibility(View.GONE);
        int locations[] = { 0, 0 };
        v.getLocationInWindow(locations);

        mDragWinLP = new WindowManager.LayoutParams();
        mDragWinLP.gravity = Gravity.LEFT | Gravity.TOP;
        mDragWinLP.x = locations[0];
        mDragWinLP.y = locations[1] - statusBarHeight;
        mDragWinLP.width = v.getWidth();
        mDragWinLP.height = v.getHeight();

        v.destroyDrawingCache();
        v.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);


        dragPosition = position;
        mDragView = new ImageView(getContext());
        mDragView.setImageBitmap(bm);
        mWindowManager.addView(mDragView, mDragWinLP);
    }



    private void endDrag() {
        if (mDragView != null || mHideView != null) {
            mWindowManager.removeView(mDragView);
            mHideView.setVisibility(View.VISIBLE);

            mDragView = null;
            mHideView = null;

            array = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };

            dragPosition = INVALID_POSITION;
        }
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

    public void moveNext(int viewPos, int aniFromPos) {
        moveFromTo(viewPos, aniFromPos, aniFromPos + 1);
    }

    public void movePrev(int viewPos, int aniFromPos) {
        moveFromTo(viewPos, aniFromPos, aniFromPos - 1);
    }

    private ArrayList<Integer> mAniMap = new ArrayList<Integer>();

    private void moveFromTo(final int viewPos, final int aniFromPos, final int aniToPos) {
        int[] fLocation = getPositionRect(aniFromPos);
        final int[] tLocation = getPositionRect(aniToPos);
        final LaucherItemView view = (LaucherItemView) getChildAt(viewPos);

        view.clearAnimation();
        

        TranslateAnimation transAni = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, tLocation[0] - fLocation[0], Animation.ABSOLUTE, 0, Animation.ABSOLUTE, tLocation[1]
                - fLocation[1]);
        transAni.setDuration(400);
        transAni.setFillAfter(true);
        transAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mAniMap.add(view.getId());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.layout(tLocation[0], tLocation[1], tLocation[0] + mFeatureMetrics.childW, tLocation[1] + mFeatureMetrics.childH);
                view.setVisibility(View.VISIBLE);
                mAniMap.remove(Integer.valueOf(view.getId()));
            }
        });
        view.startAnimation(transAni);

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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout");
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


    // ------------------------------------------------------
    // 工具类
    // -------------------------------------------------------
    /**
     * 
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
