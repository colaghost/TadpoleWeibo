package com.xiaotingzhong.app;

import android.app.Application;
import android.content.res.Configuration;

import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.response.Emotion;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.keep.AccessTokenKeeper;

public class XTZApplication extends Application {
    public static XTZApplication app;

    private long curUid;
    private User curUser = null;

    public static Emotion getEmotionByPhrase(String p) {
        return Emotion.map.get(p);
    }

    public static long getCurUid() {
        return app.curUid;
    }

    public static User getCurUser() {
        return app.curUser;
    }

    public static void setCurUser(User u) {
        app.curUser = u;
        app.curUid = u.id;
    }


    public static FriendshipsAPI getFriendshipsAPI() {
        return new FriendshipsAPI(AccessTokenKeeper.readAccessToken(app));
    }

    public static UsersAPI getUsersAPI() {
        return new UsersAPI(AccessTokenKeeper.readAccessToken(app));
    }

    public static StatusesAPI getStatusesAPI() {
        return new StatusesAPI(AccessTokenKeeper.readAccessToken(app));
    }

    public static AccountAPI getAccountAPI() {
        return new AccountAPI(AccessTokenKeeper.readAccessToken(app));
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