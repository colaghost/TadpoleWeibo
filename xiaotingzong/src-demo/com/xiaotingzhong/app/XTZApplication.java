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
	public static XTZApplication app;

	private static SharedPreferences sSharedPref;

	static final String PREF_XTZ = "com_xiaotingzhong.pref";

	private long curUid;

	private User curUser = null;

	public void onCreate() {
		super.onCreate();
		app = this;
		sSharedPref = getSharedPreferences(PREF_XTZ, MODE_PRIVATE);
	}

	public static SharedPreferences getGlobalSharedPref() {
		return sSharedPref;
	}

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

	public static Oauth2AccessToken getWeiboAccessToken() {
		return AccessTokenKeeper.readAccessToken(app);
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

	public void onLowMemory() {
		super.onLowMemory();
	}

	public void onTerminate() {
		super.onTerminate();
	}

}