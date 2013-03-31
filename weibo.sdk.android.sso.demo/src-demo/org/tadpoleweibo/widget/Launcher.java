package org.tadpoleweibo.widget;

import java.util.ArrayList;

import org.tadpoleweibo.common.TLog;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPagerEX;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Launcher extends ViewPagerEX {

    static final int ANI_DURATION = 500;

    protected static final String TAG = "Launcher";
    private static final int INVALID_POSITION = -1;
    LancherListAdapter mListAdapter;
    int stopCount = 0;
    int dragPosition = INVALID_POSITION;
    int dropPosition = INVALID_POSITION;
    int draggingPage = INVALID_POSITION;
    boolean isDragging = false;

    private WindowManager mWindowManager;
    int mPageItemCount = 8;
    private DataSetObserver mDataSetObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            Log.d(TAG, "onChanged");
            Launcher.this.notifyDataUpdate();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            Log.d(TAG, "onInvalidated");
            Launcher.this.notifyDataUpdate();
        }

    };

    public Launcher(Context context) {
        super(context);
        init();
    }

    public Launcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        ArrayList<String> mStrList = new ArrayList<String>();
        for (int i = 0; i < 23; i++) {
            mStrList.add("text + " + i);
        }
        LancherListAdapter<String> test = new LancherListAdapter<String>(mStrList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(getContext());
                tv.setText(getData(position));
                return tv;
            }
        };
        this.setDataAdapter(test);
    }


    public void setDataAdapter(LancherListAdapter adapter) {
        if (mListAdapter != null && mDataSetObserver != null) {
            mListAdapter.unregisterDataSetObserver(mDataSetObserver);
            mListAdapter = null;
        }
        removeAllViews();
        mListAdapter = adapter;
        mListAdapter.registerDataSetObserver(mDataSetObserver);
        populateDataFromAdapter();
    }


    private ArrayList<LauncherPage> mLauncherPageList = new ArrayList<LauncherPage>();


    private void notifyDataUpdate() {
        if (mLauncherPageList == null) {
            return;
        }

        for (int i = 0; i < mLauncherPageList.size(); i++) {
            mLauncherPageList.get(i).onDataUpdate();
        }
    }

    private void populateDataFromAdapter() {
        int pageCount = mListAdapter.getCount() / (mPageItemCount + 1) + 1;
        for (int i = 0; i < pageCount; i++) {
            LauncherPage launcherPage = new LauncherPage(getContext(), i, Launcher.this);
            launcherPage.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            mLauncherPageList.add(launcherPage);
        }

        this.setOnPageChangeListener(new ViewPagerEX.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        this.setOffscreenPageLimit(4);
        PagerAdapter pageAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //                container.removeView((View) object);.
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                System.out.println("instantiateItem = " + position);
                LauncherPage lp = mLauncherPageList.get(position);
                container.addView(lp);
                return lp;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mLauncherPageList.size();
            }
        };
        this.setAdapter(pageAdapter);
    }

    private View mHideView = null;
    private ImageView mDragView = null;
    private WindowManager.LayoutParams mDragWinLP = null;
    private int lastX = 0;
    private int lastY = 0;


    public void startDragging(View v, int index, int pageIndex) {
        draggingPage = pageIndex;
        isDragging = true;
        dragPosition = index;

        System.out.println("dragPosition = " + dragPosition);

        final int statusBarHeight = getStatusHeight((Activity) getContext());

        mHideView = v;
        v.setVisibility(View.INVISIBLE);
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

        mDragView = new ImageView(getContext());
        mDragView.setImageBitmap(bm);
        mWindowManager.addView(mDragView, mDragWinLP);
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

                lastX = (int) (xTemp + 0.2f);
                lastY = (int) yTmep;

                mWindowManager.updateViewLayout(mDragView, mDragWinLP);
                dragging(lastX, lastY);
            }
        }

        if (MotionEvent.ACTION_CANCEL == ev.getAction() || MotionEvent.ACTION_UP == ev.getAction()) {
            endDragging();
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isDragging) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void dragging(int x, int y) {
        System.out.println("mDragWinLP.x = " + mDragWinLP.x);
        Log.d(TAG, "rightDistance = " + (mDragWinLP.x + mDragWinLP.width - getWidth()));

        final int currentItem = getCurrentItem();

        // 检测是否往下，还是往前
        if ((getWidth() - mDragWinLP.x - mDragWinLP.width) <= 0 && currentItem < mPageItemCount) {
            stopCount++;
            if (stopCount > 30) {
                stopCount = 0;
                setCurrentItem(currentItem + 1);
                if (currentItem != draggingPage) {
                    this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLauncherPageList.get(currentItem).onReset();
                        }
                    }, 200);
                }
            }
        } else if (mDragWinLP.x <= 0 && currentItem > 0) {
            stopCount++;
            if (stopCount > 30) {
                stopCount = 0;
                setCurrentItem(currentItem - 1);
                if (currentItem != draggingPage) {
                    this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLauncherPageList.get(currentItem).onReset();
                        }
                    }, 200);
                }
            }
        } else {
            stopCount = 0;
        }

        mLauncherPageList.get(this.getCurrentItem()).onDragging(x, y);
    }

    public void endDragging() {
        // 先执行Item，才能获取下落的位置
        mLauncherPageList.get(this.getCurrentItem()).onEndDrag();
        if (mDragView != null || mHideView != null) {
            mWindowManager.removeView(mDragView);
            mHideView.setVisibility(View.VISIBLE);
            mDragView = null;
            mHideView = null;
            TLog.debug(TAG, "onSwapPosition f = %d, t = %d", dragPosition, dropPosition);

            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dragPosition != dropPosition) {
                        if (dragPosition != INVALID_POSITION && dropPosition != INVALID_POSITION) {
                            mListAdapter.onSwapPosition(dragPosition, dropPosition);
                        }
                    }

                    dragPosition = INVALID_POSITION;
                    dropPosition = INVALID_POSITION;
                    draggingPage = INVALID_POSITION;

                    stopCount = 0;
                    isDragging = false;

                }
            }, ANI_DURATION);
        }
    }

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

    static int[] sLocation = { 0, 0 };

    public void flyTo(View childAt, Animation animation) {
        childAt.setVisibility(View.INVISIBLE);
        childAt.startAnimation(animation);
    }

    private ViewGroup aniViewGroup = null;

    public View copyViewInAniLayer(View view, int height, int width) {
        if (aniViewGroup == null) {
            aniViewGroup = createAnimLayout();
        }
        int location[] = new int[2];
        view.getLocationInWindow(location);
        ImageView imageView = new ImageView(getContext());
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(view.getDrawingCache());
        imageView.setImageBitmap(bm);
        addViewToAnimLayout(imageView, location, height, width);
        return imageView;
    }

    /**
     * @Description: 创建动画层
     * @param
     * @return void
     * @throws
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
        RelativeLayout animLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * @Description: 添加视图到动画层
     * @param @param vg
     * @param @param view
     * @param @param location
     * @param @return
     * @return View
     * @throws
     */
    public View addViewToAnimLayout(final View view, int[] location, int height, int width) {
        int x = location[0];
        int y = location[1];
        if (aniViewGroup == null) {
            aniViewGroup = createAnimLayout();
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        lp.leftMargin = x;
        lp.topMargin = y;

        view.setLayoutParams(lp);
        aniViewGroup.addView(view);
        return view;
    }
}
