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
import com.weibo.sdk.android.api.response.WeiboStatuses;

public class WeiboStatusesCacheMgr extends BaseUserFileCache {
    private File mCacheDir = null;

    public WeiboStatusesCacheMgr(Context context, long uid, long friendUid) {
        super(context, uid);
        mCacheDir = getSubDir("statues/" + friendUid);
    }

    public ArrayList<WeiboStatuses> getStatusesFromCache() throws Exception {
        File[] files = this.mCacheDir.listFiles();
        if (files.length == 0) {
            return null;
        } else {
            ArrayList<WeiboStatuses> iList = new ArrayList<WeiboStatuses>();
            for (File f : files) {
                WeiboStatuses item = WeiboStatuses.fromResponse(FileUtil.readFile(f));
                if (item != null) {
                    iList.add(item);
                }
            }
            return iList;
        }

    }

    public PageList<WeiboStatuses> saveAndGetFriends(String friendsJson) throws Exception {
        JSONObject jo = new JSONObject(friendsJson);
        JSONArray jr = jo.getJSONArray("statuses");
        JSONObject joTmp = null;

        String path = this.mCacheDir.getAbsolutePath();

        ArrayList<WeiboStatuses> iList = new ArrayList<WeiboStatuses>();
        for (int i = 0, len = jr.length(); i < len; i++) {
            joTmp = jr.getJSONObject(i);
            WeiboStatuses item = WeiboStatuses.fromResponse(joTmp);
            iList.add(item);
            String str = path + File.separator + item.id;
            FileUtil.createFile(str);
            FileUtil.writeFile(str, joTmp.toString().getBytes(), false);
        }

        PageList<WeiboStatuses> pageList = new PageList<WeiboStatuses>();
        pageList.records = iList;
        pageList.total_number = jo.optInt("total_number");
        pageList.next_cursor = jo.optInt("next_cursor");
        pageList.previous_cursor = jo.optInt("previous_cursor");
        return pageList;
    }
}