package com.xiaotingzhong.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.xiaotingzhong.model.Emotion;
import com.xiaotingzhong.model.User;

public class XTZApplication extends Application {
	public static XTZApplication sApp;

	private static SharedPreferences sSharedPref;

	static final String PREF_XTZ = "com_xiaotingzhong.pref";

	private long mCurUid;

	private User mCurUser = null;

	public void onCreate() {
		super.onCreate();
		sApp = this;
		sSharedPref = getSharedPreferences(PREF_XTZ, MODE_PRIVATE);
	}

	public static SharedPreferences getGlobalSharedPref() {
		return sSharedPref;
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

}