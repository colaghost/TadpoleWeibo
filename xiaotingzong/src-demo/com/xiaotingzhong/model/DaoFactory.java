
package com.xiaotingzhong.model;

import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.model.cache.UsersCache;
import com.xiaotingzhong.model.cache.userprivate.FriendsCache;
import com.xiaotingzhong.model.cache.userprivate.SubscriptionCache;
import com.xiaotingzhong.model.dao.IFriendsDao;
import com.xiaotingzhong.model.dao.ISubscriptionDao;
import com.xiaotingzhong.model.dao.IUserDao;

public class DaoFactory {

    /**
     * @param ownerUid 拥有该用户缓存的微博用户id
     * @return
     */
    public static IFriendsDao getFriendsDao(long ownerUid) {
        return new FriendsCache(XTZApplication.app, ownerUid);
    }

    /**
     * @param ownerUid 拥有该用户缓存的微博用户id
     * @return
     */
    public static ISubscriptionDao getSubscriptionDao(long ownerUid) {
        return new SubscriptionCache(XTZApplication.app, ownerUid);
    }

    public static IUserDao getUserDao(long ownerUid) {
        return new UsersCache(XTZApplication.app);
    }

}
