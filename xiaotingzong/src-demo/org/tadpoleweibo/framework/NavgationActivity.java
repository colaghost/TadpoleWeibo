
package org.tadpoleweibo.framework;

import org.tadpole.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.Stack;

public class NavgationActivity extends Activity {

    private static final String TAG = "TpNavBarActivity";

    private static final ViewGroup.LayoutParams LP_F_F = new LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.FILL_PARENT);

    private NavBarImpl mNavBar;

    private LinearLayout mRootLayout;

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
        addViewToLayout(mRootLayout, view, params);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    private Stack<AbstractTpWindow> mWindowList = new Stack<AbstractTpWindow>();

    public void pushWindow(AbstractTpWindow window) {

        if (mWindowList.size() > 0) {
            AbstractTpWindow w = mWindowList.peek();
            w.getRoot().startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
            ViewGroup vg = (ViewGroup)w.getRoot().getParent();
            vg.removeView(w.getRoot());
        }

        mWindowList.push(window);
        LinearLayout root = createVerticalLayout(this);
        super.setContentView(root);
        NavBarImpl navBar = new NavBarImpl(this);
        navBar.createAndAttacthTo(root);
        //
        window.setRoot(root);
        window.setActivity(this);
        window.setNavBar(navBar);
        navBar.setListener(window);

        // add Window view
        View view = window.onCreate();
        if (null == view) {
            Log.w(TAG, "view can't not be null");
            return;
        }
        addViewToLayout(root, view, null);
    }

    public void popWindow() {
        AbstractTpWindow window = mWindowList.pop();
        if (mWindowList.size() == 0) {
            finish();
        } else {
            AbstractTpWindow win = mWindowList.peek();
            ViewGroup root = win.getRoot();
            super.setContentView(root);
        }
    }

    public INavBar getNavBar() {
        return getNavBarImpl();
    }

    private void ensureRootLayout() {
        if (null == mRootLayout) {
            mRootLayout = createVerticalLayout(this);
            super.setContentView(mRootLayout);
            getNavBarImpl().createAndAttacthTo(mRootLayout);
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

    private static LinearLayout createVerticalLayout(Activity act) {
        LinearLayout layout = new LinearLayout(act);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.RED);
        layout.setLayoutParams(LP_F_F);
        return layout;
    }

    private static void addViewToLayout(ViewGroup layout, View view, LayoutParams params) {

        // remvoe other
        int childCount = layout.getChildCount();
        if (childCount >= 2) {
            for (int i = childCount; i > 1; i--) {
                layout.removeViewAt(i);
            }
        }

        if (null == params) {
            params = LP_F_F;
        }

        // add view
        layout.addView(view, params);
    }

    private NavBarImpl getNavBarImpl() {
        if (mNavBar == null) {
            mNavBar = new NavBarImpl(this);
        }
        return mNavBar;
    }

}
