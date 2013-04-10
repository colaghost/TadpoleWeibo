package com.xiaotingzhong.app;

import com.weibo.sdk.android.api.response.User;

import android.app.Application;
import android.content.res.Configuration;

public class XTZApplication extends Application {
    public static XTZApplication app;

    public int curUid;
    public User curUser;

    public void onConfigurationChanged(Configuration paramConfiguration) {
        super.onConfigurationChanged(paramConfiguration);
    }

    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onTerminate() {
        super.onTerminate();
    }
}