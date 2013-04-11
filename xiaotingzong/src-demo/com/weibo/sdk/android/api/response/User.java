package com.weibo.sdk.android.api.response;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class User implements Serializable {

    private static final long serialVersionUID = 7431294131953990833L;

    public int id;
    public String screen_name;
    public String name;
    public String profile_image_url;
    public String avatar_large;
    
    
    
    public static ArrayList<User> fromFriends(String response) throws Exception {
        JSONObject jsonObj = new JSONObject(response);
        JSONArray usersArr = jsonObj.getJSONArray("users");
        ArrayList<User> userList = new ArrayList<User>();
        for (int i = 0, len = usersArr.length(); i < len; i++) {
            userList.add(User.fromResponse(usersArr.getJSONObject(i)));
        }
        return userList;
    }
    
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
