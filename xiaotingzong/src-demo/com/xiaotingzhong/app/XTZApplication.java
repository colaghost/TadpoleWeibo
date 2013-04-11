package com.xiaotingzhong.app;

import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.keep.AccessTokenKeeper;

import android.app.Application;
import android.content.res.Configuration;

public class XTZApplication extends Application {
    public static XTZApplication app;

    public int curUid;
    public User curUser;


    public static FriendshipsAPI getFriendshipsAPI() {
        return new FriendshipsAPI(AccessTokenKeeper.readAccessToken(app));
    }

    public static UsersAPI getUsersAPI() {
        return new UsersAPI(AccessTokenKeeper.readAccessToken(app));
    }

    public static StatusesAPI getStatusesAPI() {
        return new StatusesAPI(AccessTokenKeeper.readAccessToken(app));
    }

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