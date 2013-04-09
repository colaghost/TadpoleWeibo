package com.xiaotingzhong.app.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tadpoleweibo.common.FileUtil;
import org.tadpoleweibo.widget.PageList;

import android.content.Context;

import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.builder.UserBuilder;

public class FriendsCacheMgr extends BaseUserFileCache {
    private File mFriendsDir = getSubDir("friends" + File.separator);

    public FriendsCacheMgr(Context paramContext, int uid) {
        super(paramContext, uid);
    }

    public ArrayList<User> getFriendsByUids(ArrayList<Integer> list) {
        ArrayList iList = new ArrayList();
        Iterator localIterator = list.iterator();
        while (true) {
            if (!localIterator.hasNext())
                return iList;
            int i = ((Integer) localIterator.next()).intValue();
            String str = this.mFriendsDir.getAbsolutePath() + File.separator + i;
            Object localObject = null;
            try {
                User user = UserBuilder.fromResponse(FileUtil.readFile(new File(str)));
                localObject = user;
                if (localObject == null)
                    continue;
                iList.add(localObject);
            } catch (IOException localIOException) {
                while (true)
                    localIOException.printStackTrace();
            }
        }
    }

    public PageList<User> getFriendsFromCache() throws Exception {
        ArrayList iList = new ArrayList();
        File[] arrayOfFile = this.mFriendsDir.listFiles();
        int i = arrayOfFile.length;
        for (int j = 0;; j++) {
            if (j >= i) {
                PageList pageList = new PageList();
                pageList.records = iList;
                pageList.totalCount = iList.size();
                pageList.nextStartIndex = 0;
                pageList.prevPage = 0;
                return pageList;
            }
            iList.add(UserBuilder.fromResponse(FileUtil.readFile(arrayOfFile[j])));
        }
    }

    public PageList<User> saveAndGetFriends(String paramString) throws Exception {
        JSONObject jo = new JSONObject(paramString);
        JSONArray jr = jo.getJSONArray("users");
        ArrayList iList = new ArrayList();
        int i = 0;
        int j = jr.length();
        while (true) {
            if (i >= j) {
                PageList pageList = new PageList();
                pageList.records = iList;
                pageList.totalCount = jo.optInt("total_number");
                pageList.nextStartIndex = jo.optInt("next_cursor");
                pageList.prevPage = jo.optInt("previous_cursor");
                return pageList;
            }
            JSONObject jo2 = jr.getJSONObject(i);
            User user = UserBuilder.fromResponse(jo2);
            iList.add(user);
            String str = this.mFriendsDir.getAbsolutePath() + File.separator + user.id;
            FileUtil.createFile(str);
            FileUtil.writeFile(str, jo2.toString().getBytes(), false);
            i++;
        }
    }
}