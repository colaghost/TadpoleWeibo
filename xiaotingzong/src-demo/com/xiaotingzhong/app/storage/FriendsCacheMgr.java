package com.xiaotingzhong.app.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tadpoleweibo.common.FileUtil;
import org.tadpoleweibo.widget.PageList;

import android.content.Context;

import com.weibo.sdk.android.api.response.User;

public class FriendsCacheMgr extends BaseUserFileCache {
    private File mFriendsDir = null;

    public FriendsCacheMgr(Context context, long uid) {
        super(context, uid);
        mFriendsDir = getSubDir("friends");
    }

    public ArrayList<User> getFriendsByUids(ArrayList<Long> uids) {
        ArrayList<User> iList = new ArrayList<User>();
        for (Long uid : uids) {
            String str = this.mFriendsDir.getAbsolutePath() + File.separator + uid;
            User user = null;
            try {
                user = User.fromResponse(FileUtil.readFile(new File(str)));
                iList.add(user);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iList;
    }

    public ArrayList<User> getFriendsFromCache() throws Exception {
        File[] friendFiles = this.mFriendsDir.listFiles();
        if (friendFiles.length == 0) {
            return null;
        } else {
            ArrayList<User> iList = new ArrayList<User>();
            for (File f : friendFiles) {
                iList.add(User.fromResponse(FileUtil.readFile(f)));
            }
            return iList;
        }

    }

    public PageList<User> saveAndGetFriends(String friendsJson) throws Exception {
        JSONObject jo = new JSONObject(friendsJson);
        JSONArray jr = jo.getJSONArray("users");
        JSONObject joTmp = null;

        String path = this.mFriendsDir.getAbsolutePath();

        ArrayList<User> iList = new ArrayList<User>();
        for (int i = 0, len = jr.length(); i < len; i++) {
            joTmp = jr.getJSONObject(i);
            User user = User.fromResponse(joTmp);
            iList.add(user);
            String str = path + File.separator + user.id;
            System.out.println("str = " + str);
            FileUtil.createFile(str);
            FileUtil.writeFile(str, joTmp.toString().getBytes(), false);
        }

        PageList<User> pageList = new PageList<User>();
        pageList.records = iList;
        pageList.total_number = jo.optInt("total_number");
        pageList.next_cursor = jo.optInt("next_cursor");
        pageList.previous_cursor = jo.optInt("previous_cursor");
        return pageList;
    }
}