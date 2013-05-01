
package com.xiaotingzhong.model;

import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.model.dao.IFriendsDao;
import com.xiaotingzhong.model.dao.ISubscriptionDao;
import com.xiaotingzhong.model.dao.IUserDao;
import com.xiaotingzhong.model.state.UserState;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tadpoleweibo.common.JSONUtil;
import org.tadpoleweibo.common.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = 7431294131953990833L;

    public long id;

    public String screen_name;

    public String name;

    public String profile_image_url;

    public String avatar_large;

    /**
     * 获取两个用户之间的关系状态
     * 
     * @param relateUser
     * @return
     */
    public UserState getRelateUserState(User relateUser) {
        UserState userState = new UserState();
        userState.isMyFriend = isMyFriend(relateUser.id);
        userState.hasSubscript = hasSubcript(relateUser.id);
        return userState;
    }

    private boolean isMyFriend(long relateUid) {
        IFriendsDao dao = DaoFactory.getFriendsDao(this.id);
        return dao.exists(relateUid);
    }

    private boolean hasSubcript(long relateUid) {
        ISubscriptionDao dao = DaoFactory.getSubscriptionDao(this.id);
        return dao.isSubscripted(relateUid);
    }

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
        String response = getResponseFromShowJson(screen_name, uid);
        User user = User.fromResponse(response);
        return user;
    }

    public static String getResponseFromShowJson(String screen_name, long uid) throws Exception {
        String response = null;
        // prefer uid
        if (uid != 0) {
            response = XTZApplication.getUsersAPI().show(uid);
        } else if (StringUtil.isNotEmpty(screen_name)) {
            response = XTZApplication.getUsersAPI().show(screen_name);
        }
        return response;
    }

    public static User getUserFromShowJson(long uid) throws Exception {
        return getUserFromShowJson("", uid);
    }

    public static String getResponseFromShowJson(long uid) throws Exception {
        return getResponseFromShowJson("", uid);
    }

    public void subscript(User user) {
        IUserDao userDao = DaoFactory.getUserDao(this.id);
        ISubscriptionDao subscriptDao = DaoFactory.getSubscriptionDao(this.id);
        // save user info
        userDao.saveUser(user.id, user.toJsonString());

        // subscript user
        subscriptDao.subscript(user.id);
    }

    private String toJsonString() {
        JSONObject jsonObj = JSONUtil.object2Json(this, User.class);
        return jsonObj.toString();
    }

    public void unSubscript(User user) {
        ISubscriptionDao subscriptDao = DaoFactory.getSubscriptionDao(this.id);
        subscriptDao.unSubscript(user.id);
    }
}
