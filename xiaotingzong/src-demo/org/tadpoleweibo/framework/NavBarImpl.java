
package org.tadpoleweibo.framework;

import org.tadpole.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

class NavBarImpl implements INavBar, View.OnClickListener {

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
    public INavBar setTitle(String title) {
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
    public INavBar setListener(NavBarListener l) {
        mListener = l;
        return this;
    }

    @Override
    public ImageButton getBtnLeft() {
        return mBtnLeft;
    }

    @Override
    public ImageButton getBtnRight() {
        return mBtnRight;
    }
}
