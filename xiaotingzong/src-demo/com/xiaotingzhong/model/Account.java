package com.xiaotingzhong.model;

import org.json.JSONObject;

import android.content.SharedPreferences;
import android.media.audiofx.AcousticEchoCanceler;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.model.cache.TokenAccountCache;

public class Account {
	public int uid;

	public static Account fromGetUid(String response) throws Exception {
		Account ac = null;
		if (response != null) {
			JSONObject jsonObj = new JSONObject(response);
			ac = new Account();
			ac.uid = jsonObj.optInt("uid", 0);
		}
		return ac;
	}

	public static User getUserPreferCache() throws Exception {

		// 使用缓存
		TokenAccountCache tokenAccount = new TokenAccountCache(
				XTZApplication.getWeiboAccessToken());
		if (tokenAccount.exist()) {
			return tokenAccount.getUser();
		}

		// 请求服务器
		else {
			AccountAPI acountAPI = XTZApplication.getAccountAPI();
			Account account = fromGetUid(acountAPI.getUid());
			String response = User.getResponseFromShowJson(account.uid);
			User user = User.fromResponse(response);
			tokenAccount.saveUserRaw(response);
			return user;
		}
	}
}
