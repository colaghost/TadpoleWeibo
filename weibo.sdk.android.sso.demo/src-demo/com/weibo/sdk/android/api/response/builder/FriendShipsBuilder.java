package com.weibo.sdk.android.api.response.builder;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.weibo.sdk.android.api.response.User;

public class FriendShipsBuilder {

    public static ArrayList<User> fromFriends(String response) throws Exception {
        JSONObject jsonObj = new JSONObject(response);
        JSONArray usersArr = jsonObj.getJSONArray("users");
        ArrayList<User> userList = new ArrayList<User>();
        for (int i = 0, len = usersArr.length(); i < len; i++) {
            userList.add(UserBuilder.fromResponse(usersArr.getJSONObject(i)));
        }
        return userList;
    }
}
