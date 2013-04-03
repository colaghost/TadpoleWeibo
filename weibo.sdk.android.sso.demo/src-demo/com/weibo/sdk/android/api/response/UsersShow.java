package com.weibo.sdk.android.api.response;

import org.json.JSONException;
import org.json.JSONObject;

public class UsersShow {
    public String screen_name;
    public String name;
    public String profile_image_url;
    public String avatar_large;


    public static UsersShow fromResponse(String response) {
        UsersShow ret = new UsersShow();
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
}
