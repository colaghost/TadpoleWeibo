
package org.tadpoleweibo.framework;

import org.tadpole.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

public class TpNavigationActivity extends Activity {

    private static final String TAG = "TpNavBarActivity";

    private static final ViewGroup.LayoutParams LP_F_F = new LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.FILL_PARENT);

    private NavBarImpl mNavBar;

    private LinearLayout mRootLayout;;

    public static interface ITpNavBar {
        public ITpNavBar setTitle(String title);

        public ITpNavBar setListener(NavBarListener l);

        public NavButton getBtnLeft();

        public NavButton getBtnRight();

    }

    public static interface NavBarListener {
        public void onDefaultLeftBtnClick(ITpNavBar navBar, View v);

        public void onDefaultRightBtnClick(ITpNavBar navBar, View v);
    }

    public static final class NavButton {

        private ImageButton mImgBtn;

        /* package */NavButton(ImageButton btn) {
            mImgBtn = btn;
        }

        public void setImageResource(int resId) {
            mImgBtn.setImageResource(resId);
        }

        public void setVisibility(int v) {
            mImgBtn.setVisibility(v);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        setContentView(view, null);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        ensureRootLayout();
        addViewToRootLayout(view, params);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    private Stack<AbstractTpWindow> mWindowList = new Stack<AbstractTpWindow>();

    public void pushWindow(AbstractTpWindow window) {
        ensureRootLayout();

        mWindowList.push(window);

        //
        window.setActivity(this);
        window.setNavBar(mNavBar);

        // add Window view
        View view = window.onCreate();
        if (null == view) {
            Log.w(TAG, "view can't not be null");
        }
        addViewToRootLayout(view, null);
    }

    public void popWindow() {
        AbstractTpWindow window = mWindowList.pop();
    }

    public ITpNavBar getNavBar() {
        return getNavBarImpl();
    }

    private void ensureRootLayout() {
        if (null == mRootLayout) {
            mRootLayout = new LinearLayout(this);
            mRootLayout.setOrientation(LinearLayout.VERTICAL);
            mRootLayout.setBackgroundColor(Color.RED);
            mRootLayout.setLayoutParams(LP_F_F);
            super.setContentView(mRootLayout);
            getNavBarImpl().createAndAttacthTo(mRootLayout);
        }
    }

    private void addViewToRootLayout(View view, LayoutParams params) {

        // remvoe other
        int childCount = mRootLayout.getChildCount();
        if (childCount >= 2) {
            for (int i = childCount; i > 1; i--) {
                mRootLayout.removeViewAt(i);
            }
        }

        if (null == params) {
            params = LP_F_F;
        }

        // add view
        mRootLayout.addView(view, params);
    }

    private NavBarImpl getNavBarImpl() {
        if (mNavBar == null) {
            mNavBar = new NavBarImpl(this);
        }
        return mNavBar;
    }

    private static class NavBarImpl implements ITpNavBar, View.OnClickListener {

        private static final String TAG = "NavBarImpl";

        private static final LayoutParams LP_F_F = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);

        private TextView mTextViewTitle;

        private Activity mActivity;

        private ImageButton mBtnLeft;

        private ImageButton mBtnRight;

        private NavBarListener mListener;

        public NavBarImpl(Activity activity) {
            mActivity = activity;
        }

        public void createAndAttacthTo(LinearLayout layout) {
            View view = mActivity.getLayoutInflater().inflate(R.layout.tp_bar_nav, layout, false);
            layout.addView(view);

            mTextViewTitle = (TextView)view.findViewById(R.id.tp_nav_title);
            mBtnLeft = (ImageButton)view.findViewById(R.id.tp_nav_btn_left);
            mBtnRight = (ImageButton)view.findViewById(R.id.tp_nav_btn_right);
            mBtnLeft.setOnClickListener(this);
            mBtnRight.setOnClickListener(this);
        }

        @Override
        public ITpNavBar setTitle(String title) {
            mTextViewTitle.setText(title);
            return this;
        }

        @Override
        public void onClick(View v) {
            final NavBarListener l = mListener;
            if (l == null) {
                mActivity.finish();
                return;
            }
            switch (v.getId()) {
                case R.id.tp_nav_btn_left:
                    l.onDefaultLeftBtnClick(this, v);
                    break;
                case R.id.tp_nav_btn_right:
                    l.onDefaultRightBtnClick(this, v);
                    break;
                default:
                    break;
            }

        }

        @Override
        public ITpNavBar setListener(NavBarListener l) {
            mListener = l;
            return this;
        }

        @Override
        public NavButton getBtnLeft() {
            return new NavButton(mBtnLeft);
        }

        @Override
        public NavButton getBtnRight() {
            return new NavButton(mBtnRight);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (null == mWindowList) {
            return;
        }

        for (AbstractTpWindow window : mWindowList) {
            window.onDestroy();
        }
    };

}
