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
import com.weibo.sdk.android.api.response.builder.UserBuilder;

public class FriendsCacheMgr extends BaseUserFileCache {
    private File mFriendsDir = null;

    public FriendsCacheMgr(Context context, int uid) {
        super(context, uid);
        mFriendsDir = getSubDir("friends");
    }

    public ArrayList<User> getFriendsByUids(ArrayList<Integer> uids) {
        ArrayList<User> iList = new ArrayList<User>();
        for (Integer uid : uids) {
            String str = this.mFriendsDir.getAbsolutePath() + File.separator + uid;
            User user = null;
            try {
                user = UserBuilder.fromResponse(FileUtil.readFile(new File(str)));
                iList.add(user);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iList;
    }

    public PageList<User> getFriendsFromCache() throws Exception {
        File[] friendFiles = this.mFriendsDir.listFiles();
        if (friendFiles.length == 0) {
            return null;
        } else {
            ArrayList<User> iList = new ArrayList<User>();
            for (File f : friendFiles) {
                iList.add(UserBuilder.fromResponse(FileUtil.readFile(f)));
            }
            PageList<User> pageList = new PageList<User>();
            pageList.records = iList;
            pageList.totalCount = iList.size();
            pageList.nextStartIndex = 0;
            pageList.prevPage = 0;
            return pageList;
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
            User user = UserBuilder.fromResponse(joTmp);
            iList.add(user);
            String str = path + File.separator + user.id;
            System.out.println("str = " + str);
            FileUtil.createFile(str);
            FileUtil.writeFile(str, joTmp.toString().getBytes(), false);
        }

        PageList<User> pageList = new PageList<User>();
        pageList.records = iList;
        pageList.totalCount = jo.optInt("total_number");
        pageList.nextStartIndex = jo.optInt("next_cursor");
        pageList.prevPage = jo.optInt("previous_cursor");
        return pageList;
    }
}