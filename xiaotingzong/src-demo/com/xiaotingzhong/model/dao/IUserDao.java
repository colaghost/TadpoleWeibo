
package com.xiaotingzhong.model.dao;

import com.xiaotingzhong.model.User;

public interface IUserDao {

    boolean exist(long uid);

    boolean saveUser(long uid, String response);

    User readUser(long uid) throws Exception;
}
