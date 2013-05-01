package com.xiaotingzhong.model.cache;

import org.tadpoleweibo.common.StringUtil;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.model.User;

public class TokenAccountCache {

	private Oauth2AccessToken mToken = null;
	private SharedPreferences mPref = null;
	private String mCacheKey = null;

	public TokenAccountCache(Oauth2AccessToken token) {
		mToken = token;
		mPref = XTZApplication.getGlobalSharedPref();
		mCacheKey = "account" + mToken.getToken();
	}

	public boolean exist() {
		if (!mPref.contains(mCacheKey)) {
			return false;
		}
		String acountCache = mPref.getString(mCacheKey, null);
		return StringUtil.isNotBlank(acountCache);
	}

	public User getUser() throws Exception {
		if (!mPref.contains(mCacheKey)) {
			return null;
		}
		String acountCache = mPref.getString(mCacheKey, null);
		return User.fromResponse(acountCache);
	}

	public void saveUserRaw(String userShowResponse) {
		Editor editor = mPref.edit();
		editor.putString(mCacheKey, userShowResponse);
		editor.commit();
	}
}
