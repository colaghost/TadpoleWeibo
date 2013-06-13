
package org.tadpoleweibo.framework;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractTpWindow implements NavBarListener {

    private INavBar mNavBar;

    private ViewGroup mRoot;

    private NavgationActivity mActivity;

    void setActivity(NavgationActivity activity) {
        mActivity = activity;
    }

    void setNavBar(INavBar navBar) {
        mNavBar = navBar;
    }

    void setRoot(ViewGroup root) {
        mRoot = root;
    }

    ViewGroup getRoot() {
        return mRoot;
    }

    protected INavBar getNavBar() {
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

    public void pushWindow(AbstractTpWindow window) {
        mActivity.pushWindow(window);
    }

    /**
     * 销毁
     */
    public void onDestroy() {

    }

    @Override
    public void onDefaultLeftBtnClick(INavBar navBar, View v) {
        mActivity.popWindow();
    }

    @Override
    public void onDefaultRightBtnClick(INavBar navBar, View v) {

    }

}
