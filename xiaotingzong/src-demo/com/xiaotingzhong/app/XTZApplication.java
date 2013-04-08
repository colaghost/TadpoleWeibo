package com.xiaotingzhong.app;

import com.weibo.sdk.android.api.response.Account;

import android.app.Application;
import android.content.res.Configuration;

public class XTZApplication extends Application {

    public Account curLoginAccount = null;

    public static XTZApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
