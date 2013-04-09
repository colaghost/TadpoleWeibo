package com.weibo.sdk.android.api.response;

import org.json.JSONException;
import org.json.JSONObject;

public class Account {
    public int uid;

    public static Account fromGetUid(String response) {
        Account ac = new Account();
        if (response != null) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                ac.uid = jsonObj.optInt("uid", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ac;
    }
}
