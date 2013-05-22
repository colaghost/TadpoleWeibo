
package com.xiaotingzhong.model.dao;

import com.xiaotingzhong.model.User;

import org.tadpoleweibo.common.FileUtil;
import org.tadpoleweibo.common.StringUtil;

import java.io.File;
import java.util.ArrayList;

public interface IUserDao {

    boolean exist(long uid);

    boolean saveUser(long uid, String response);

    User readUser(long uid) throws Exception;
}
