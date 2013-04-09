package com.weibo.sdk.android.api.response.builder;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Paint.Join;

import com.weibo.sdk.android.api.response.User;

public class UserBuilder {
    public static User fromResponse(String response) throws Exception {
        User ret = new User();
        if (response != null) {
            JSONObject jsonObj = new JSONObject(response);
            return fromResponse(jsonObj);
        }
        return ret;
    }

    public static User fromResponse(JSONObject jsonObj) throws Exception {
        User ret = new User();
        ret.id = jsonObj.getInt("id");
        ret.profile_image_url = jsonObj.getString("profile_image_url");
        ret.screen_name = jsonObj.optString("screen_name");
        ret.avatar_large = jsonObj.optString("avatar_large");
        ret.name = jsonObj.optString("name");
        return ret;
    }
}
