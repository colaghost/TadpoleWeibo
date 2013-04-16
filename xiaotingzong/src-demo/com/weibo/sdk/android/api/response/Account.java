package com.weibo.sdk.android.api.response;

import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.xiaotingzhong.app.XTZApplication;

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

    public static User getUserByToken() throws Exception {
        AccountAPI acountAPI = XTZApplication.getAccountAPI();
        Account account = fromGetUid(acountAPI.getUid());
        User user = User.getUserFromShowJson(account.uid);
        return user;
    }
}
