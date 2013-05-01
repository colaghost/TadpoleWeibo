
package com.xiaotingzhong.model.cache.userprivate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tadpoleweibo.common.FileUtil;
import org.tadpoleweibo.common.StringUtil;
import org.tadpoleweibo.widget.PageList;

import android.content.Context;

import com.xiaotingzhong.model.DaoFactory;
import com.xiaotingzhong.model.User;
import com.xiaotingzhong.model.cache.BaseUserPrivateDir;
import com.xiaotingzhong.model.dao.IFriendsDao;
import com.xiaotingzhong.model.dao.IUserDao;

public class FriendsCache extends BaseUserPrivateDir implements IFriendsDao {
    private File mFriendsDir = null;

    private IUserDao mUserDao = null;

    public FriendsCache(Context context, long uid) {
        super(context, uid);
        mFriendsDir = createSubDir("friends");
        mUserDao = DaoFactory.getUserDao(uid);
    }

    public boolean exists(long friendUid) {
        if (friendUid == 0) {
            return false;
        }

        File[] friendFiles = this.mFriendsDir.listFiles();
        if (friendFiles.length == 0) {
            return false;
        }

        for (File f : friendFiles) {
            if (StringUtil.equals("" + friendUid, f.getName())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getUsersByUids(ArrayList<Long> uids) {
        ArrayList<User> iList = new ArrayList<User>();
        User user = null;
        for (Long uid : uids) {
            try {
                user = mUserDao.readUser(uid);
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
            long uid = 0;
            for (File f : friendFiles) {
                try {
                    uid = Long.valueOf(f.getName());
                    iList.add(mUserDao.readUser(uid));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

            // save user to user cache
            mUserDao.saveUser(user.id, joTmp.toString());

            // save user friend cache
            FileUtil.createFile(str);
        }

        PageList<User> pageList = new PageList<User>();
        pageList.records = iList;
        pageList.total_number = jo.optInt("total_number");
        pageList.next_cursor = jo.optInt("next_cursor");
        pageList.previous_cursor = jo.optInt("previous_cursor");
        return pageList;
    }
}
