package com.weibo.sdk.android.api.response.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.sdk.android.api.response.User;

public class UserBuilder {
    public static User fromResponse(String response) {
        User ret = new User();
        if (response != null) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                ret.profile_image_url = jsonObj.optString("profile_image_url");
                ret.screen_name = jsonObj.optString("screen_name");
                ret.avatar_large = jsonObj.optString("avatar_large");
                ret.name = jsonObj.optString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static User fromResponse(JSONObject jsonObj) {
        User ret = new User();
        try {
            ret.profile_image_url = jsonObj.getString("profile_image_url");
            ret.screen_name = jsonObj.optString("screen_name");
            ret.avatar_large = jsonObj.optString("avatar_large");
            ret.name = jsonObj.optString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
