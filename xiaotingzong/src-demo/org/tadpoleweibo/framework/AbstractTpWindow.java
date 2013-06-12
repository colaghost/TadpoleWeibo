
package org.tadpoleweibo.framework;

import org.tadpoleweibo.framework.TpNavigationActivity.ITpNavBar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;

public abstract class AbstractTpWindow {

    private ITpNavBar mNavBar;

    private Activity mActivity;

    void setActivity(Activity activity) {
        mActivity = activity;
    }

    void setNavBar(ITpNavBar navBar) {
        mNavBar = navBar;
    }

    protected ITpNavBar getNavBar() {
        return mNavBar;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public Intent getIntent() {
        return mActivity.getIntent();
    }

    public LayoutInflater getLayoutInflater() {
        return mActivity.getLayoutInflater();
    }

    public Resources getResources() {
        return mActivity.getResources();
    }

    public void runOnUiThread(Runnable action) {
        mActivity.runOnUiThread(action);
    }

    public abstract View onCreate();

    /**
     * 销毁
     */
    public void onDestroy() {

    }

}
