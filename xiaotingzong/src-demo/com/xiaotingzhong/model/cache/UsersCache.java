
package com.xiaotingzhong.model.cache;

import com.xiaotingzhong.model.User;
import com.xiaotingzhong.model.dao.IUserDao;

import org.tadpoleweibo.common.FileUtil;
import org.tadpoleweibo.common.StringUtil;

import android.content.Context;

import java.io.File;

public class UsersCache extends AbsCacheDir implements IUserDao {

    static final String DIR_USER = "users";

    public UsersCache(Context context) {
        super(context, DIR_USER);
    }

    public boolean exist(long uid) {
        return existSubFile("" + uid);
    }

    public boolean saveUser(long uid, String response) {
        if (StringUtil.isBlank(response)) {
            return false;
        }
        File file = createSubFile("" + uid);
        return FileUtil.writeFile(file, response);
    }

    public User readUser(long uid) throws Exception {
        String str = readSubFile("" + uid);
        return User.fromResponse(str);
    }

}
