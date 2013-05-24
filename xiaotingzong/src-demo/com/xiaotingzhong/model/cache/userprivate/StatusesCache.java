
package com.xiaotingzhong.model.cache.userprivate;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tadpoleweibo.common.FileUtil;
import org.tadpoleweibo.widget.PageList;

import android.content.Context;

import com.xiaotingzhong.model.WeiboStatus;
import com.xiaotingzhong.model.cache.BaseUserPrivateDir;

public class StatusesCache extends BaseUserPrivateDir {
    private File mCacheDir = null;

    public StatusesCache(Context context, long uid, long friendUid) {
        super(context, uid);
        mCacheDir = createSubDir("statues/" + friendUid);
    }

    public ArrayList<WeiboStatus> getStatusesFromCache() throws Exception {
        File[] files = this.mCacheDir.listFiles();
        if (files.length == 0) {
            return null;
        } else {
            ArrayList<WeiboStatus> iList = new ArrayList<WeiboStatus>();
            for (File f : files) {
                WeiboStatus item = WeiboStatus.fromResponse(FileUtil.readFile(f));
                if (item != null) {
                    iList.add(item);
                }
            }
            return iList;
        }

    }

    public PageList<WeiboStatus> saveAndGetStatuses(String jsonStr) throws Exception {
        JSONObject jo = new JSONObject(jsonStr);
        JSONArray jr = jo.getJSONArray("statuses");
        JSONObject joTmp = null;

        String path = this.mCacheDir.getAbsolutePath();

        ArrayList<WeiboStatus> iList = new ArrayList<WeiboStatus>();
        for (int i = 0, len = jr.length(); i < len; i++) {
            joTmp = jr.getJSONObject(i);
            WeiboStatus item = WeiboStatus.fromResponse(joTmp);
            iList.add(item);
            String str = path + File.separator + item.id;
            FileUtil.createFile(str);
            FileUtil.writeFile(str, joTmp.toString().getBytes(), false);
        }

        PageList<WeiboStatus> pageList = new PageList<WeiboStatus>();
        pageList.records = iList;
        pageList.total_number = jo.optInt("total_number");
        pageList.next_cursor = jo.optInt("next_cursor");
        pageList.previous_cursor = jo.optInt("previous_cursor");
        return pageList;
    }
}
