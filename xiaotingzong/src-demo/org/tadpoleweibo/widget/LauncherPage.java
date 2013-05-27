
package org.tadpoleweibo.widget;

import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.tadpole.R;
import org.tadpoleweibo.common.TLog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class LauncherPage extends ViewGroup {
    static final String TAG = "LauncherPage";

    static final Rect RECT = new Rect();

    private int mPageDraPos = INVALID_POSITION;

    private int mPageDropPos = INVALID_POSITION;

    public static final int[] LOCALTION = {
            0, 0
    };

    private FeatureMetrics mFeatureMetrics = new FeatureMetrics();

    private WindowManager mWindowManager;

    private int[] mArray = new int[] {
            0, 1, 2, 3, 4, 5, 6, 7
    };

    private int mPageIndex;

    private Launcher mLauncher;

    private int mItemCount = 0;

    private int mLastPageItemPos = 0;

    public LauncherPage(Context context, int pageIndex, Launcher launcher) {
        super(context, null, R.style.Tadpole_LauncherPage);

        mPageIndex = pageIndex;
        mLauncher = launcher;
        mWindowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        resetDragParams();

        Resources resource = getContext().getResources();
        int paddingLeft = resource.getDimensionPixelOffset(R.dimen.launcher_page_padding_left);
        int paddingTop = resource.getDimensionPixelOffset(R.dimen.launcher_page_padding_top);
        int paddingRight = resource.getDimensionPixelOffset(R.dimen.launcher_page_padding_right);
        int paddingBottom = resource.getDimensionPixelOffset(R.dimen.launcher_page_padding_bottom);

        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        fillData();
    }

    private LauncherPageItemView createLauncherPageItemView(final int position) {
        final ListAdapter listAdapter = mLauncher.mListAdapter;
        LauncherPageItemView itemView = new LauncherPageItemView(getContext());
        View view = listAdapter.getView(position, null, null);
        final long itemId = listAdapter.getItemId(position);

        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
        itemView.addView(view);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLauncher.onItemClickListener(v, position, itemId);
            }
        });
        return itemView;
    }

    public void fillData() {
        final ListAdapter listAdapter = mLauncher.mListAdapter;
        final int pageItemCount = mLauncher.mPageItemCount;

        for (int i = 0, len = getChildCount(); i < len; i++) {
            getChildAt(i).clearAnimation();
        }
        removeAllViews();

        if (listAdapter != null) {
            int start = mPageIndex * pageItemCount;
            int end = listAdapter.getCount() - start;
            if (end > pageItemCount) {
                end = (mPageIndex + 1) * pageItemCount;
            } else {
                end = listAdapter.getCount();
            }

            mLastPageItemPos = end - mPageIndex * pageItemCount - 1;

            TLog.debug(TAG, "fillData start = %d end = %d mPageIndex =", start, end, mPageIndex);
            for (int i = start; i < end; i++) {
                LauncherPageItemView itemView = createLauncherPageItemView(i);
                final int index = i;
                itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mHideView = v;

                        LauncherPageItemView dragView = createLauncherPageItemView(index);
                        mLauncher.startDragging(v, index, mPageIndex, dragView);
                        // covert by pageIndex;
                        mPageDraPos = index - mPageIndex * mLauncher.mPageItemCount;
                        mPageDropPos = mPageDraPos;

                        return false;
                    }
                });
                this.addView(itemView);
                this.onLayoutInternal();
            }
        }
    }

    public void onDataUpdate() {
        fillData();
        onReset();
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
        int[] location = null;
        for (int i = 0; i < mItemCount; i++) {
            location = getLocationByPos(i);
            rect.set(location[0], location[1], location[0] + mFeatureMetrics.childW, location[1]
                    + mFeatureMetrics.childH);
            if (rect.contains(x, y)) {
                return i;
            }
        }
        return INVALID_POSITION;
    }

    int[] getLocationPrevPageByPos(int position) {
        int[] location = getLocationByPos(position);
        location[0] -= getWidth();
        return location;
    }

    int[] getLocationNextPageByPos(int position) {
        int[] location = getLocationByPos(position);
        location[0] += getWidth();
        return location;
    }

    int[] getWindowLocationByPos(int position) {
        int[] location = getLocationByPos(position);
        location[1] += getStatusHeight((Activity)getContext()) + mFeatureMetrics.titleBarHeight;
        return location;
    }

    void onDragging(int x, int y) {
        int hitPosition = getHitPosition(x, y);
        final int[] arr = mArray;

        // 非拖动元素所属该页
        if (mPageDraPos == INVALID_POSITION) {
            int[] fromLocation = null;
            int[] toLocation = null;
            Animation ani = null;
            if (mLauncher.getCurrentItem() > mLauncher.lastPageItem) {

                // Launcher
                int from = mPageIndex * mLauncher.mPageItemCount;
                int to = from - 1;
                mLauncher.mListAdapter.moveFromTo(from, to);
                mLauncher.notifyDataUpdate(mPageIndex - 1);

                // Cur Page
                mPageDraPos = 0;
                mPageDropPos = 0;

                fromLocation = getLocationByPos(0);
                toLocation = getLocationPrevPageByPos(mLauncher.mPageItemCount - 1);

            } else {

                // Launcher
                int to = (mPageIndex + 1) * mLauncher.mPageItemCount;
                int from = to - 1;
                mLauncher.mListAdapter.moveFromTo(from, to);
                mLauncher.notifyDataUpdate(mPageIndex + 1);

                // Cur Page
                mPageDraPos = mLauncher.mPageItemCount - 1;
                mPageDropPos = mPageDraPos;
                fromLocation = getLocationByPos(mLauncher.mPageItemCount - 1);
                toLocation = getLocationNextPageByPos(0);
            }

            mLauncher.mDropPosition = mPageIndex * mLauncher.mPageItemCount + mPageDraPos;

            ani = getTranslateAnimation(fromLocation, toLocation);
            View view = getChildAt(mPageDraPos);
            view.setVisibility(View.INVISIBLE);
            View copyViewAni = mLauncher.copyViewInAniLayer(view,
                    getWindowLocationByPos(mPageDraPos), mFeatureMetrics.childH,
                    mFeatureMetrics.childW);
            mLauncher.attachToAniAndStartAni(copyViewAni, ani, null);

        }

        if (hitPosition >= getChildCount()) {
            return;
        }

        int realDragPostion = arr[mPageDraPos];
        // Log.d(TAG, "dragging hitPosition = " + hitPosition +
        // ", realDragPostion =" + realDragPostion);
        if (hitPosition == realDragPostion || hitPosition == INVALID_POSITION) {
            return;
        }

        TLog.debug("", "hitPosition hitPosition = %d,realDragPostion = %d ,mPageDraPos =%d",
                hitPosition, realDragPostion, mPageDraPos);
        HashMap<Integer, Integer> posToViewPos = getRealArrayHashMap();

        // move next
        if (hitPosition < realDragPostion) {
            for (int i = hitPosition; i < realDragPostion; i++) {
                moveNext(posToViewPos.get(i), i, null);
                arr[posToViewPos.get(i)] += 1;
            }
        }
        // move prev
        else {
            for (int i = hitPosition; i > realDragPostion; i--) {
                movePrev(posToViewPos.get(i), i, null);
                arr[posToViewPos.get(i)] -= 1;
            }
        }
        arr[mPageDraPos] = hitPosition;

        mPageDropPos = hitPosition;

        System.out.println("arr = " + Arrays.toString(arr));
    }

    public HashMap<Integer, Integer> getRealArrayHashMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < mArray.length; i++) {
            hashMap.put(mArray[i], i);
        }
        return hashMap;
    }

    int onEndDrag() {
        Log.d(TAG, "mLauncher.mDragPosition = " + mLauncher.mDragPosition + ", mPageDropPos = "
                + mPageDropPos);
        int launcherDropPosition = INVALID_POSITION;
        // 制造假得view，等待数据刷新.
        if (mLauncher.mDragPosition != INVALID_POSITION) {
            final View fakeView = createLauncherPageItemView(mLauncher.mDragPosition);
            if (fakeView != null) {
                putViewToPostion(fakeView, mPageDropPos);
            }
        }

        // 设置当前释放
        if (mPageDropPos != INVALID_POSITION) {
            launcherDropPosition = mPageIndex * mLauncher.mPageItemCount + mPageDropPos;
        }
        resetDragParams();

        return launcherDropPosition;
    }

    private void putViewToPostion(View view, int position) {
        this.addView(view);
        int[] location = getLocationByPos(position);
        view.measure(MeasureSpec.makeMeasureSpec(mFeatureMetrics.childW, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mFeatureMetrics.childH, MeasureSpec.EXACTLY));
        view.layout(location[0], location[1], location[0] + mFeatureMetrics.childW, location[1]
                + mFeatureMetrics.childH);
    }

    private void resetDragParams() {
        mItemCount = mLauncher.mNumColumns * mLauncher.mNumRows;
        mArray = new int[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mArray[i] = i;
        }

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
        final int numColums = mLauncher.COL_COUNT;

        int v = position / numColums; // 第几行
        int z = position % numColums; // 第几列

        int xTmp = x + z * childW + z * cSpace;
        int yTmp = y + v * childH + v * rSpace;

        return new int[] {
                xTmp, yTmp
        };
    }

    public void moveNext(int viewPos, int aniFromPos, AnimationListener listener) {
        moveFromTo(viewPos, aniFromPos, aniFromPos + 1, listener);
    }

    public void movePrev(int viewPos, int aniFromPos, AnimationListener listener) {
        moveFromTo(viewPos, aniFromPos, aniFromPos - 1, listener);
    }

    private void moveFromTo(final int viewPos, final int aniFromPos, final int aniToPos,
            AnimationListener listener) {
        int[] fLocation = getLocationByPos(aniFromPos);
        final int[] tLocation = getLocationByPos(aniToPos);
        final LauncherPageItemView view = (LauncherPageItemView)getChildAt(viewPos);

        if (view == null) {
            return;
        }

        view.forceAniEnd();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                view.layout(tLocation[0], tLocation[1], tLocation[0] + mFeatureMetrics.childW,
                        tLocation[1] + mFeatureMetrics.childH);
                view.setVisibility(View.VISIBLE);
            }
        };
        view.setEndRunnable(runnable);

        Animation transAni = getTranslateAnimation(fLocation, tLocation);
        view.setAnimationListener(listener);
        view.startAnimation(transAni);
    }

    private Animation getTranslateAnimation(int[] fromLocation, int[] toLocation) {
        TranslateAnimation transAni = new TranslateAnimation(Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, toLocation[0] - fromLocation[0], Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, toLocation[1] - fromLocation[1]);
        transAni.setDuration(Launcher.ANI_DURATION);
        return transAni;
    }

    /**
     * 属性值计算缓存 <br>=
     * ========================= <br>
     * author：Zenip <br>
     * email：lxyczh@gmail.com <br>
     * create：2013-3-29 <br>=
     * =========================
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

            childW = (measureWidth - pLeft - pRight - ((view.mLauncher.mNumColumns - 1) * cSpace))
                    / view.mLauncher.mNumColumns;
            childH = (measureHeight - pTop - pBottom - (view.mLauncher.mNumRows - 1) * rSpace)
                    / view.mLauncher.mNumRows;

            isCaculate = true;

            Activity act = (Activity)view.getContext();
            Window window = act.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(RECT);
            statusBarHeight = RECT.top;

            int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
            titleBarHeight = contentViewTop - statusBarHeight;

        }

        int pLeft, pRight, pTop, pBottom, rSpace, cSpace, childW, childH, titleBarHeight,
                statusBarHeight;

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

    private boolean mIsInited = false;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mFeatureMetrics.calculate(this);
        if (mIsInited == false) {
            onLayoutInternal();
            mIsInited = true;
        }
        super.setMeasuredDimension(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    public void onLayoutInternal() {
        final int x = mFeatureMetrics.pLeft; // 左padding
        final int y = mFeatureMetrics.pTop; // 右padding
        final int childW = mFeatureMetrics.childW; // 子控件宽度
        final int childH = mFeatureMetrics.childH; // 子控件高度
        final int cSpace = mFeatureMetrics.cSpace; // 列间距
        final int rSpace = mFeatureMetrics.rSpace; // 行间距
        final int numColums = mLauncher.mNumColumns; // 列数量

        int v = 0;
        int z = 0;
        int xTmp, yTmp;

        int childCount = getChildCount();
        // Log.d(TAG, "childCount = " + childCount);

        View childView = null;
        for (int i = 0; i < childCount; i++) {
            childView = getChildAt(i);

            v = i / numColums; // 第几行
            z = i % numColums; // 第几列

            xTmp = x + z * childW + z * cSpace;
            yTmp = y + v * childH + v * rSpace;

            childView.measure(MeasureSpec.makeMeasureSpec(childW, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(childH, MeasureSpec.EXACTLY));
            // TLog.debug(TAG,
            // "onLayout left = %d , top = %d, right = %d, bottom = %d", xTmp,
            // yTmp, xTmp + childW, yTmp + childH);
            childView.layout(xTmp, yTmp, xTmp + childW, yTmp + childH);
        }
    }

    // ------------------------------------------------------
    // 工具类
    // -------------------------------------------------------
    /**
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
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
                        .toString());
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

    public void onDelete(int pageItemPos, final int launcherPageItemPos,
            final Runnable deleteCallback) {
        Log.d(TAG, "onDelete  mLastPageItemPos = " + mLastPageItemPos + ", click pageItemPos =  "
                + pageItemPos + ", launcherPageItemPos = " + launcherPageItemPos);
        final View v = getChildAt(pageItemPos);

        v.setVisibility(View.INVISIBLE);
        Animation dimissAni = getDismissAnimation();
        dimissAni.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.clearAnimation();
                v.setVisibility(View.INVISIBLE);
            }
        });
        v.startAnimation(dimissAni);

        boolean isNeedDragFromNextPage = true;

        Log.d(TAG, "onDelete mPageIndex = " + mPageIndex + ",  mLauncher.mPageItemCount = "
                + mLauncher.mPageItemCount);

        int fromPos = (mPageIndex + 1) * mLauncher.mPageItemCount;

        Log.d(TAG,
                "onDelete fromPos = " + fromPos + ", mLauncher.getPageCount() = "
                        + mLauncher.getPageCountFromPageAdapter() + ", mLauncher.mListAdapter.getCount() =  "
                        + mLauncher.mListAdapter.getCount());
        if ((mLauncher.getPageCountFromPageAdapter() <= (mPageIndex + 1))
                || (fromPos > (mLauncher.mListAdapter.getCount() - 1))) {
            isNeedDragFromNextPage = false;
        }

        // 无动画
        boolean hasAnimation = false;
        // 有动画
        for (int i = mLastPageItemPos; i > pageItemPos; i--) {
            hasAnimation = true;
            if ((i == pageItemPos + 1) && isNeedDragFromNextPage == false) {
                movePrev(i, i, new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.d(TAG, "onDelete finish with inner animation ");
                        mLauncher.mListAdapter.remove(launcherPageItemPos);
                        mLauncher.notifyDataUpdate();
                        if (deleteCallback != null) {
                            deleteCallback.run();
                        }
                    }
                });
            } else {
                movePrev(i, i, null);
            }
        }

        if (hasAnimation == false && isNeedDragFromNextPage == false) {
            Log.d(TAG, "onDelete finish without animation");
            mLauncher.mListAdapter.remove(launcherPageItemPos);
            mLauncher.notifyDataUpdate();
            if (deleteCallback != null) {
                deleteCallback.run();
            }
            return;
        }

        if (isNeedDragFromNextPage == false) {
            return;
        }

        final View view = createLauncherPageItemView(fromPos);
        final View viewFroAni = createLauncherPageItemView(fromPos);
        final int toPageItemPos = mLauncher.mPageItemCount - 1;

        int[] toLocation = getLocationByPos(mLauncher.mPageItemCount - 1);
        int[] fromLocation = getLocationNextPageByPos(0);

        Animation ani = getTranslateAnimation(fromLocation, toLocation);

        int[] fromWindowLocatioin = {
                0, 0
        };
        System.arraycopy(fromLocation, 0, fromWindowLocatioin, 0, 2);
        fromWindowLocatioin[1] += getStatusHeight((Activity)getContext())
                + mFeatureMetrics.titleBarHeight;

        mLauncher.addViewToAnimLayout(viewFroAni, fromWindowLocatioin, mFeatureMetrics.childH,
                mFeatureMetrics.childW);
        mLauncher.attachToAniAndStartAni(viewFroAni, ani, new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "onDelete finish with outer animation ");
                mLauncher.mListAdapter.remove(launcherPageItemPos);
                putViewToPostion(view, toPageItemPos);
                mLauncher.notifyDataUpdate();
                if (deleteCallback != null) {
                    deleteCallback.run();
                }
            }
        });
    }

    public Animation getDismissAnimation() {
        ScaleAnimation scaleAni = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAni.setDuration(Launcher.ANI_DURATION - 200);
        return scaleAni;
    }

};
