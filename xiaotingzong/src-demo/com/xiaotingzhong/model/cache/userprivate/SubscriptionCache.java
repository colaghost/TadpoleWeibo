
package com.xiaotingzhong.model.cache.userprivate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.tadpoleweibo.common.StringUtil;

import android.content.Context;

import com.xiaotingzhong.model.cache.BaseUserPrivateDir;
import com.xiaotingzhong.model.dao.ISubscriptionDao;

public class SubscriptionCache extends BaseUserPrivateDir implements ISubscriptionDao {
    static final String SUBSCRIPT_DIR = "subscript";

    private File mSubscriptFile = null;

    public SubscriptionCache(Context context, long uid) {
        super(context, uid);
        mSubscriptFile = createSubFile(SUBSCRIPT_DIR);
    }

    public ArrayList<Long> getSubscriptedUids() {
        ArrayList<Long> uidLst = new ArrayList<Long>();
        FileInputStream in = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            in = new FileInputStream(mSubscriptFile);
            isr = new InputStreamReader(in);
            br = new BufferedReader(isr);

            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (StringUtil.isNotBlank(line)) {
                    uidLst.add(Long.valueOf(line));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(br);
            close(isr);
            close(in);
        }
        return uidLst;
    }

    public boolean isSubscripted(long uid) {
        return getSubscriptedUids().contains(new Long(uid));
    }

    public void subscript(long uid) {
        ArrayList<Long> uidLst = getSubscriptedUids();
        uidLst.add(uid);
        saveSubscript(uidLst);
    }

    public void saveSubscript(ArrayList<Long> uidLst) {
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        PrintWriter pw = null;
        try {
            out = new FileOutputStream(mSubscriptFile);
            osw = new OutputStreamWriter(out);
            pw = new PrintWriter(osw);
            for (Long uid : uidLst) {
                pw.write("" + uid + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(pw);
            close(osw);
            close(out);
        }
    }

    public void unSubscript(long uid) {
        ArrayList<Long> uidList = getSubscriptedUids();
        uidList.remove(new Long(uid));
        saveSubscript(uidList);
    }
}
