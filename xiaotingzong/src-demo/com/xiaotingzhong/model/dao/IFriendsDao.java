
package com.xiaotingzhong.model.dao;

import com.xiaotingzhong.model.User;

import java.util.ArrayList;

public interface IFriendsDao {

    boolean exists(long relateUid);

    ArrayList<User> getUsersByUids(ArrayList<Long> subscriptedUids);

}
