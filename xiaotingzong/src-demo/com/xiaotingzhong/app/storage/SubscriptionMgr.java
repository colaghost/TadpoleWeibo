package com.xiaotingzhong.app.storage;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONObject;
import org.tadpoleweibo.common.FileUtil;


public class SubscriptionMgr extends BaseUserFileCache {
    static final String SUBSCRIPT_DIR = "subscript";
    private File mSubscriptDir = null;

    public SubscriptionMgr(Context context, int uid) {
        super(context, uid);
        mSubscriptDir = getSubDir(SUBSCRIPT_DIR);
    }

    public ArrayList<Integer> getSubscriptedUids() {
        ArrayList<Integer> uidLst = new ArrayList<Integer>();
        File[] files = this.mSubscriptDir.listFiles();
        for (File f : files) {
            uidLst.add(Integer.valueOf(f.getName()));
        }
        return uidLst;
    }

    public boolean isSubscripted(int uid) {
        return new File(this.mSubscriptDir.getAbsolutePath() + File.separator + uid).exists();
    }

    public void subscript(int uid) {
        SubsriptItem localSubsriptItem = new SubsriptItem();
        FileUtil.writeFile(this.mSubscriptDir.getAbsolutePath() + File.separator + uid, localSubsriptItem.toJSONBytes(), false);
    }

    public void unSubscript(int uid) {
        FileUtil.delete(this.mSubscriptDir.getAbsolutePath() + File.separator + uid);
    }

    public static final class SubsriptItem {
        public byte[] toJSONBytes() {
            return toJSONObject().toString().getBytes();
        }

        public JSONObject toJSONObject() {
            return new JSONObject();
        }

        public String toJSONString() {
            return toJSONObject().toString();
        }
    }
}