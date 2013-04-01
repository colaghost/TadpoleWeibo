package org.tadpoleweibo.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPagerEX;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weibo.sdk.android.demo.R;

public class Launcher extends ViewPagerEX {

    static final int ANI_DURATION = 500;
    static final String TAG = "Launcher";
    private static final int INVALID_POSITION = -1;

    static final int ROW_COUNT = 3;
    static final int COL_COUNT = 3;

    int mNumColumns = COL_COUNT;
    int mNumRows = ROW_COUNT;

    LauncherListAdapter mListAdapter;
    int mEdgeStopCount = 0;
    int mDragPosition = INVALID_POSITION;
    int mDropPosition = INVALID_POSITION;
    int mPageItemCount = ROW_COUNT * COL_COUNT;
    PagerAdapter mPageAdapter;
    ImageView mDragView = null;
    WindowManager.LayoutParams mDragWinLP = null;


    private int mLastX = 0;
    private int mLastY = 0;

    private boolean mIsDragging = false;
    private ArrayList<LauncherPage> mLauncherPageList = new ArrayList<LauncherPage>();
    private WindowManager mWindowManager;
    private DataSetObserver mDataSetObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            Log.d(TAG, "onChanged");
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            Log.d(TAG, "onInvalidated");
        }

    };
    private boolean isHandlingDelete = false;

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
        final ArrayList<String> mStrList = new ArrayList<String>();
        for (int i = 0; i < 23; i++) {
            mStrList.add("text + " + i);
        }
        LauncherListAdapter<String> test = new LauncherListAdapter<String>(mStrList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                final View view = LayoutInflater.from(getContext()).inflate(R.layout.launche_page_item, null);
                TextView textView = (TextView) view.findViewById(R.id.pageItemText);
                View deleteBtnView = view.findViewById(R.id.pageItemDeleteBtn);

                String item = mStrList.get(position);
                textView.setText(item);

                final View bg = view.findViewById(R.id.pageItemBg);
                deleteBtnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(position);
                    }
                });

                //                if (BoardPageItem.COLOR_BLUE.equals(item.color)) {
                bg.setBackgroundResource(R.drawable.blue);
                //                } else {
                //                    bg.setBackgroundResource(R.drawable.red);
                //                }

                //                if (Configure.isEditMode) {
                bg.getBackground().setAlpha(220);
                //                    if (item.editable) {
                deleteBtnView.setVisibility(View.VISIBLE);
                //                    } else {
                //                deleteBtnView.setVisibility(View.INVISIBLE);
                //                    }
                //                } else {
                //                    bg.getBackground().setAlpha(255);
                //                    deleteBtnView.setVisibility(View.INVISIBLE);
                //                }

                //                if (item.hide) {
                //                    view.setVisibility(View.INVISIBLE);
                //                }
                return view;
            }
        };
        this.setDataAdapter(test);
    }


    public void setDataAdapter(LauncherListAdapter adapter) {
        if (mListAdapter != null && mDataSetObserver != null) {
            mListAdapter.unregisterDataSetObserver(mDataSetObserver);
            mListAdapter = null;
        }
        removeAllViews();
        mListAdapter = adapter;
        mListAdapter.registerDataSetObserver(mDataSetObserver);
        mListAdapter.setLauncher(this);
        populateDataFromAdapter();
    }

    void notifyDataUpdate() {
        if (mLauncherPageList == null) {
            return;
        }
        for (int i = 0; i < mLauncherPageList.size(); i++) {
            mLauncherPageList.get(i).onDataUpdate();
        }
    }

    void notifyDataUpdate(int pageIndex) {
        LauncherPage page = mLauncherPageList.get(pageIndex);
        page.onDataUpdate();
    }

    private void populateDataFromAdapter() {
        int pageCount = (mListAdapter.getCount() - 1) / mPageItemCount + 1;
        for (int i = 0; i < pageCount; i++) {
            LauncherPage launcherPage = new LauncherPage(getContext(), i, Launcher.this);
            launcherPage.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            mLauncherPageList.add(launcherPage);
        }

        this.setOffscreenPageLimit(mLauncherPageList.size());
        mPageAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
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
        this.setAdapter(mPageAdapter);
    }


    public void startDragging(View v, int index, int pageIndex) {
        mIsDragging = true;
        mDragPosition = index;

        lastPageItem = pageIndex;

        System.out.println("dragPosition = " + mDragPosition);

        final int statusBarHeight = getStatusHeight((Activity) getContext());

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
            mLastX = (int) ev.getRawX();
            mLastY = (int) ev.getRawY();
        }
        if (MotionEvent.ACTION_MOVE == ev.getAction()) {
            if (mDragView != null) {
                float xTemp = ev.getRawX();
                float yTmep = ev.getRawY();

                mDragWinLP.x += xTemp - mLastX;
                mDragWinLP.y += yTmep - mLastY;

                mLastX = (int) (xTemp + 0.2f);
                mLastY = (int) yTmep;

                mWindowManager.updateViewLayout(mDragView, mDragWinLP);
                dragging(mLastX, mLastY);
            }
        }

        if (MotionEvent.ACTION_CANCEL == ev.getAction() || MotionEvent.ACTION_UP == ev.getAction()) {
            if (mIsDragging) {
                endDragging();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsDragging) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isHandlingDelete == true) {
            return true;
        }

        if (mIsDragging) {
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public void dragging(int x, int y) {
        if (getScrollState() != SCROLL_STATE_IDLE) {
            return;
        }


        final int currentItem = getCurrentItem();

        System.out.println("mDragWinLP.x = " + mDragWinLP.x);
        Log.d(TAG, "rightDistance = " + (getWidth() - mDragWinLP.x - mDragWinLP.width));

        // 检测是否往下，还是往前
        if ((getWidth() - mDragWinLP.x - mDragWinLP.width) <= 0 && currentItem < getPageCount()) {
            mEdgeStopCount++;
            if (mEdgeStopCount > 30) {
                mEdgeStopCount = 0;
                lastPageItem = currentItem;
                setCurrentItem(currentItem + 1, true);
            }
        } else if (mDragWinLP.x <= 0 && currentItem > 0) {
            mEdgeStopCount++;
            if (mEdgeStopCount > 30) {
                mEdgeStopCount = 0;
                lastPageItem = currentItem;
                setCurrentItem(currentItem - 1, true);
            }
        } else {
            mEdgeStopCount = 0;
        }

        mLauncherPageList.get(this.getCurrentItem()).onDragging(x, y);
    }

    public void endDragging() {
        // 先执行当前Page，
        mDropPosition = mLauncherPageList.get(this.getCurrentItem()).onEndDrag();

        if (mDragView != null) {
            mWindowManager.removeView(mDragView);
            mDragView = null;
            if (mDragPosition != mDropPosition) {
                if (mDragPosition != INVALID_POSITION && mDropPosition != INVALID_POSITION) {
                    mListAdapter.moveFromTo(mDragPosition, mDropPosition);
                }
            }
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0, len = mLauncherPageList.size(); i < len; i++) {
                        mLauncherPageList.get(i).onDataUpdate();
                    }

                    mDragPosition = INVALID_POSITION;
                    mDropPosition = INVALID_POSITION;
                    lastPageItem = INVALID_POSITION;

                    mEdgeStopCount = 0;
                    mIsDragging = false;

                }
            }, ANI_DURATION);
        }
    }

    public void delete(int launcherPageItemPos) {
        int pageIndex = launcherPageItemPos / mPageItemCount;
        int pageItemPos = launcherPageItemPos % mPageItemCount;

        final int totalPageCountAfterReduce = (mListAdapter.getCount() - 2) / mPageItemCount + 1;
        final int pageCount = getPageCount();
        Log.d(TAG, "totalPageCountAfterReduce = " + totalPageCountAfterReduce);

        isHandlingDelete = true;

        // 删除item
        mLauncherPageList.get(pageIndex).onDelete(pageItemPos, launcherPageItemPos, new Runnable() {
            @Override
            public void run() {
                isHandlingDelete = false;
                if (pageCount > totalPageCountAfterReduce) {
                    if (getCurrentItem() == totalPageCountAfterReduce) {
                        setCurrentItem(totalPageCountAfterReduce - 1, true);
                    }
                    mLauncherPageList.remove(pageCount - 1);
                    mPageAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public int getPageCount() {
        if (mPageAdapter != null) {
            return mPageAdapter.getCount();
        }
        return 0;
    }



    // -----------------------------------------------------------
    // 动画层
    // -----------------------------------------------------------
    private ViewGroup aniViewGroup = null;

    public int lastPageItem = INVALID_POSITION;

    public View copyViewInAniLayer(View view, int[] location, int height, int width) {
        if (aniViewGroup == null) {
            aniViewGroup = createAnimLayout();
        }
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

    public void attachToAniAndStartAni(final View view, Animation animation, final AnimationListener listener) {
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (listener != null) {
                    listener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (listener != null) {
                    listener.onAnimationRepeat(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
                view.clearAnimation();
                if (aniViewGroup != null) {
                    aniViewGroup.removeView(view);
                }
            }
        });
    }


    // -----------------------------------------------------------
    // 工具方法
    // -----------------------------------------------------------

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
