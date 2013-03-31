package org.tadpoleweibo.widget;

import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.tadpoleweibo.common.TLog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.weibo.sdk.android.demo.R;

public class LauncherPage extends ViewGroup {

    static final String TAG = "Launcher";
    static final int ROW_COUNT = 4;
    private int mNumColums = 2;

    private int mPageDraPos = INVALID_POSITION;
    private int mPageDropPos = INVALID_POSITION;
    public static final int LOCALTION[] = { 0, 0 };

    private FeatureMetrics mFeatureMetrics = new FeatureMetrics();
    private WindowManager mWindowManager;

    private int array[] = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    private int mPageIndex;
    private Launcher mLauncher;

    public void fillData() {
        final ListAdapter listAdapter = mLauncher.mListAdapter;
        final int pageItemCount = mLauncher.mPageItemCount;

        removeAllViews();

        if (listAdapter != null) {
            int start = mPageIndex * pageItemCount;
            int end = listAdapter.getCount() - start;
            if (end > pageItemCount) {
                end = (mPageIndex + 1) * pageItemCount;
            } else {
                end = listAdapter.getCount();
            }

            TLog.debug(TAG, "fillData start = %d end = %d", start, end);
            for (int i = start; i < end; i++) {
                LaucherItemView itemView = new LaucherItemView(getContext());
                View view = listAdapter.getView(i, null, null);
                view.setBackgroundColor(Color.GRAY);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                itemView.addView(view);
                itemView.setBackgroundColor(Color.RED);

                final int index = i;
                itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mHideView = v;
                        mLauncher.startDragging(v, index, mPageIndex);
                        // covert by pageIndex;
                        mPageDraPos = index - mPageIndex * mLauncher.mPageItemCount;

                        return false;
                    }
                });
                this.addView(itemView);
                this.requestLayout();
            }
        }
    }


    public void onDataUpdate() {
        fillData();
    }

    public LauncherPage(Context context, int pageIndex, Launcher launcher) {
        super(context, null, R.style.Tadpole_LauncherPage);
        mPageIndex = pageIndex;
        mLauncher = launcher;
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);


        Resources resource = getContext().getResources();
        int paddingLeft = resource.getDimensionPixelOffset(R.dimen.launcher_page_padding_left);
        int paddingTop = resource.getDimensionPixelOffset(R.dimen.launcher_page_padding_top);
        int paddingRight = resource.getDimensionPixelOffset(R.dimen.launcher_page_padding_right);
        int paddingBottom = resource.getDimensionPixelOffset(R.dimen.launcher_page_padding_bottom);

        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        fillData();
    }

    private int getRowSpacing() {
        return 20;
    }

    private int getColumnSpacing() {
        return 20;
    }

    private View mHideView;

    static final int INVALID_POSITION = -1;

    private int getHitPosition(int x, int y) {
        Rect rect = new Rect();
        int location[] = null;
        for (int i = 0; i < 8; i++) {
            location = getLocationByPos(i);
            rect.set(location[0], location[1], location[0] + mFeatureMetrics.childW, location[1] + mFeatureMetrics.childH);
            if (rect.contains(x, y)) {
                return i;
            }
        }
        return INVALID_POSITION;
    }

    int[] getLocationPrevPageByPos(int position) {
        int location[] = getLocationByPos(position);
        location[0] -= getWidth();
        return location;
    }

    int[] getLocationNextPageByPos(int position) {
        int location[] = getLocationByPos(position);
        location[0] += getWidth();
        return location;
    }

    void onDragging(int x, int y) {
        int hitPosition = getHitPosition(x, y);
        final int[] arr = array;

        // 非拖动元素所属该页
        if (mPageDraPos == INVALID_POSITION) {
            mPageDraPos = 0;
            int []fromLocation = null;
            int [] toLocation = null;
            Animation  ani = null;
            if (mLauncher.getCurrentItem() > mLauncher.draggingPage) {
                mPageDraPos = 0;
                fromLocation = getLocationByPos(0);
                toLocation = getLocationPrevPageByPos(mLauncher.mPageItemCount - 1);
                ani = getTranslateAnimation(fromLocation, toLocation);
                
                mLauncher.flyTo(getChildAt(mPageDraPos), ani);
            } else {
                mPageDraPos = mLauncher.mPageItemCount - 1;
                
                fromLocation = getLocationByPos(mLauncher.mPageItemCount - 1);
                toLocation = getLocationNextPageByPos(0);
                ani = getTranslateAnimation(fromLocation, toLocation);
                
                mLauncher.flyTo(getChildAt(mPageDraPos), ani);
            }
        }

        int realDragPostion = arr[mPageDraPos];
        //        Log.d(TAG, "dragging hitPosition = " + hitPosition + ", realDragPostion =" + realDragPostion);
        if (hitPosition == realDragPostion || hitPosition == INVALID_POSITION) {
            return;
        }

        TLog.debug("", "hitPosition hitPosition = %d,realDragPostion = %d ,mPageDraPos =%d", hitPosition, realDragPostion, mPageDraPos);
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
        arr[mPageDraPos] = hitPosition;

        mPageDropPos = hitPosition;

        System.out.println("arr = " + Arrays.toString(arr));
    }

    public HashMap<Integer, Integer> getRealArrayHashMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < array.length; i++) {
            hashMap.put(array[i], i);
        }
        return hashMap;
    }

    void onEndDrag() {
        if (mHideView != null) {
            if (mPageDropPos != INVALID_POSITION) {
                mLauncher.dropPosition = mPageIndex * mLauncher.mPageItemCount + mPageDropPos;
                int location[] = getLocationByPos(mPageDropPos);
                mHideView.layout(location[0], location[1], location[0] + mFeatureMetrics.childW, location[1] + mFeatureMetrics.childH);
            }
            mHideView.setVisibility(View.VISIBLE);
            mHideView = null;
        }
        resetDragParams();
    }

    private void resetDragParams() {
        array = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        mPageDraPos = INVALID_POSITION;
        mPageDropPos = INVALID_POSITION;
    }

    int[] getLocationByPos(int position) {
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

    private void moveFromTo(final int viewPos, final int aniFromPos, final int aniToPos) {
        int[] fLocation = getLocationByPos(aniFromPos);
        final int[] tLocation = getLocationByPos(aniToPos);
        final LaucherItemView view = (LaucherItemView) getChildAt(viewPos);

        if (view == null) {
            return;
        }

        view.forceAniEnd();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                view.layout(tLocation[0], tLocation[1], tLocation[0] + mFeatureMetrics.childW, tLocation[1] + mFeatureMetrics.childH);
                view.setVisibility(View.VISIBLE);
            }
        };
        view.setEndRunnable(runnable);

        Animation transAni = getTranslateAnimation(fLocation, tLocation);
        view.startAnimation(transAni);
    }

    private Animation getTranslateAnimation(int[] fromLocation, int[] toLocation) {
        TranslateAnimation transAni = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, toLocation[0] - fromLocation[0], Animation.ABSOLUTE, 0, Animation.ABSOLUTE, toLocation[1]
                - fromLocation[1]);
        transAni.setDuration(Launcher.ANI_DURATION);
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

        void calculate(LauncherPage view) {
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

        public void print() {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("pLeft", pLeft);
                jsonObj.put("pRight", pRight);
                jsonObj.put("pTop", pTop);
                jsonObj.put("pBottom", pBottom);
                jsonObj.put("rSpace", rSpace);
                jsonObj.put("cSpace", cSpace);
                jsonObj.put("childW", childW);
                jsonObj.put("childH", childH);

                TLog.debug(TAG, "FeatureMetrics = %s", jsonObj.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout pageIndex = " + mPageIndex);
        mFeatureMetrics.calculate(this);
        onLayoutInternal();
        super.setMeasuredDimension(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    public void onLayoutInternal() {
        final int x = mFeatureMetrics.pLeft;
        final int y = mFeatureMetrics.pTop;
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

            childView.measure(MeasureSpec.makeMeasureSpec(childW, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childH, MeasureSpec.EXACTLY));
            //            TLog.debug(TAG, "onLayout left = %d , top = %d, right = %d, bottom = %d", xTmp, yTmp, xTmp + childW, yTmp + childH);
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

    public void onReset() {
        TLog.debug(TAG, "onReset pageIndex = %d", mPageIndex);
        onLayoutInternal();
        resetDragParams();
    }
}
