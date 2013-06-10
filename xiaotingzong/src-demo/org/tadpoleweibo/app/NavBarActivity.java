
package org.tadpoleweibo.app;

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

public class NavBarActivity extends Activity {

    private static final ViewGroup.LayoutParams LP_F_F = new LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.FILL_PARENT);

    private NavBarImpl mNavBar;

    private LinearLayout mLinearLayout;;

    public static interface NavBar {
        public NavBar setTitle(String title);

        public NavBar setListener(NavBarListener l);

        public NavButton getBtnLeft();

        public NavButton getBtnRight();

    }

    public static interface NavBarListener {
        public void onDefaultLeftBtnClick(NavBar navBar, View v);

        public void onDefaultRightBtnClick(NavBar navBar, View v);
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
        if (null == mLinearLayout) {
            mLinearLayout = new LinearLayout(this);
            mLinearLayout.setOrientation(LinearLayout.VERTICAL);
            mLinearLayout.setBackgroundColor(Color.RED);
            mLinearLayout.setLayoutParams(LP_F_F);
        }
   
        if (null != mLinearLayout.getParent()) {
            ((ViewGroup)(mLinearLayout.getParent())).removeView(mLinearLayout);
        }

        super.setContentView(mLinearLayout);
        getNavBarImpl().createAndAttacthTo(mLinearLayout);
        mLinearLayout.addView(view, LP_F_F);

    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    public NavBar getNavBar() {
        return getNavBarImpl();
    }

    private NavBarImpl getNavBarImpl() {
        if (mNavBar == null) {
            mNavBar = new NavBarImpl(this);
        }
        return mNavBar;
    }

    private static class NavBarImpl implements NavBar, View.OnClickListener {

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
        public NavBar setTitle(String title) {
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
        public NavBar setListener(NavBarListener l) {
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

}
