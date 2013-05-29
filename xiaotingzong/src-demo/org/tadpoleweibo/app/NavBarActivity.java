
package org.tadpoleweibo.app;

import org.tadpole.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavBarActivity extends Activity {

    private NavBar mNavBar;

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

    public NavBar getNavBar() {
        if (mNavBar == null) {
            mNavBar = new NavBarImpl(this);
        }
        return mNavBar;
    }

    private static class NavBarImpl implements NavBar, View.OnClickListener {

        private static final String TAG = "NavBarImpl";
        
        private static final LayoutParams LP_F_F = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        private TextView mTextViewTitle;

        private Activity mActivity;

        private ImageButton mBtnLeft;

        private ImageButton mBtnRight;

        private NavBarListener mListener;

        public NavBarImpl(Activity activity) {
            mActivity = activity;
            createAndAttacthTo();
        }

        public void createAndAttacthTo() {
            View contentView = mActivity.findViewById(android.R.id.content);
            if (null == contentView) {
                Log.d(TAG, "findViewById(android.R.id.content) return null");
                return;
            }

            ViewGroup vg = (ViewGroup)contentView.getParent();
            if (null == vg) {
                Log.d(TAG, "findViewById(android.R.id.content).getParent() return null");
                return;
            }

            if (false == (vg instanceof LinearLayout)) {
                LinearLayout layer = new LinearLayout(mActivity);
                layer.setOrientation(LinearLayout.VERTICAL);
                
                vg.removeView(contentView);
                vg.addView(layer, LP_F_F);
                
                vg = layer;
            }else{
                ((LinearLayout)vg).setOrientation(LinearLayout.VERTICAL);
                vg.removeView(contentView);
            }

            View view = mActivity.getLayoutInflater().inflate(R.layout.tp_bar_nav, vg, false);
            vg.addView(view);
            vg.addView(contentView);

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
