package com.xiaotingzhong.app;

import android.app.Application;
import android.content.res.Configuration;

public class XTZApplication extends Application {
    public static XTZApplication app;
    public static int curUid;

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