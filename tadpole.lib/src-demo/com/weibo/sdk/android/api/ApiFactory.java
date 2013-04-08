package com.weibo.sdk.android.api;

import android.content.Context;

import com.weibo.sdk.android.keep.AccessTokenKeeper;

public class ApiFactory {
    
    public static FriendshipsAPI getFriendShipsAPI(Context context){
        return new FriendshipsAPI(AccessTokenKeeper.readAccessToken(context));
    }
}
