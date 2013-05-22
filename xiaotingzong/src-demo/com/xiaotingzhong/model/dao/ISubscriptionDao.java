
package com.xiaotingzhong.model.dao;

import java.util.ArrayList;

public interface ISubscriptionDao {

    boolean isSubscripted(long id);

    void unSubscript(long id);

    void subscript(long id);

    void saveSubscript(ArrayList<Long> uidLst);

}
