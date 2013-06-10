package com.xiaotingzhong.model;

import org.json.JSONObject;

import com.weibo.sdk.android.api.AccountAPI;
import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.model.cache.TokenAccountCache;

public class Account {
    
    private static final String TAG = "Account";
    
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
		    XTZApplication.debug(TAG, "getUserPreferCache useCache = " + true);
			return tokenAccount.getUser();
		}
		// 请求服务器
		else {
		    XTZApplication.debug(TAG, "getUserPreferCache useCache = " + false);
			AccountAPI acountAPI = XTZApplication.getAccountAPI();
			Account account = fromGetUid(acountAPI.getUid());
			String response = User.getResponseFromShowJson(account.uid);
			User user = User.fromResponse(response);
			tokenAccount.saveUserRaw(response);
			return user;
		}
	}
}
