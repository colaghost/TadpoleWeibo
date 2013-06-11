
package com.xiaotingzhong.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.provider.MediaStore.Files;
import android.util.Log;

import java.io.File;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.xiaotingzhong.model.Emotion;
import com.xiaotingzhong.model.SettingsModel;
import com.xiaotingzhong.model.User;

import org.tadpoleweibo.common.SDCardUtil;
import org.tadpoleweibo.widget.image.ImageDiskCache;

public class XTZApplication extends Application {

    public static final String SD_FOLDER_NAME = "xiaotingzong";

    public static XTZApplication sApp;

    private SharedPreferences mSharedPref;

    static final String PREF_XTZ = "com_xiaotingzhong.pref";

    private long mCurUid = 0;

    private User mCurUser = null;

    private ImageDiskCache mImageDiskCache;

    public void onCreate() {
        super.onCreate();
        sApp = this;
        mSharedPref = getSharedPreferences(PREF_XTZ, MODE_PRIVATE);
        initImageDiskCache();
    }

    private void initImageDiskCache() {
        String relatviePath = File.separator + SD_FOLDER_NAME + File.separator + "imageDiskCache";
        String imageDiskCachePath = null;
        try {
            imageDiskCachePath = SDCardUtil.getSDPath() + relatviePath;
        } catch (Exception e) {
            imageDiskCachePath = sApp.getFilesDir().getAbsolutePath() + relatviePath;
        }
        mImageDiskCache = new ImageDiskCache(imageDiskCachePath);
    }

    public static SharedPreferences getGlobalSharedPref() {
        return sApp.mSharedPref;
    }

    public static Emotion getEmotionByPhrase(String p) {
        return Emotion.MAP.get(p);
    }

    public static long getCurUid() {
        return sApp.mCurUid;
    }

    public static User getCurUser() {
        return sApp.mCurUser;
    }

    public static void setCurUser(User u) {
        sApp.mCurUser = u;
        sApp.mCurUid = u.id;
    }

    public static void debug(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    public static Oauth2AccessToken getWeiboAccessToken() {
        return AccessTokenKeeper.readAccessToken(sApp);
    }

    public static FriendshipsAPI getFriendshipsAPI() {
        return new FriendshipsAPI(AccessTokenKeeper.readAccessToken(sApp));
    }

    public static UsersAPI getUsersAPI() {
        return new UsersAPI(AccessTokenKeeper.readAccessToken(sApp));
    }

    public static StatusesAPI getStatusesAPI() {
        return new StatusesAPI(AccessTokenKeeper.readAccessToken(sApp));
    }

    public static AccountAPI getAccountAPI() {
        return new AccountAPI(AccessTokenKeeper.readAccessToken(sApp));
    }

    public void onConfigurationChanged(Configuration paramConfiguration) {
        super.onConfigurationChanged(paramConfiguration);
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onTerminate() {
        super.onTerminate();
    }

    public static SettingsModel getSettingsModel() {
        return new SettingsModel(getGlobalSharedPref());
    }

    public static ImageDiskCache getImageDiskCache() {
        return sApp.mImageDiskCache;
    }

}
