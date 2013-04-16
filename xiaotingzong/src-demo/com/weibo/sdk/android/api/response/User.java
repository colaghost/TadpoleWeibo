package com.weibo.sdk.android.api.response;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tadpoleweibo.common.StringUtil;

import com.weibo.sdk.android.WeiboException;
import com.xiaotingzhong.app.XTZApplication;

public class User implements Serializable {

    private static final long serialVersionUID = 7431294131953990833L;

    public long id;
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
        if (jsonObj == null) {
            return null;
        }

        User ret = new User();
        ret.id = jsonObj.getLong("id");
        ret.profile_image_url = jsonObj.getString("profile_image_url");
        ret.screen_name = jsonObj.optString("screen_name");
        ret.avatar_large = jsonObj.optString("avatar_large");
        ret.name = jsonObj.optString("name");
        return ret;
    }

    public static User getUserFromShowJson(String screen_name, long uid) throws Exception {
        User user = null;
        // prefer uid 
        if (uid != 0) {
            String response = XTZApplication.getUsersAPI().show(uid);
            user = User.fromResponse(response);
        } else if (StringUtil.isNotEmpty(screen_name)) {
            String response = XTZApplication.getUsersAPI().show(screen_name);
            user = User.fromResponse(response);
        }
        return user;
    }


    public static User getUserFromShowJson(long uid) throws Exception {
        return getUserFromShowJson("", uid);
    }
}
