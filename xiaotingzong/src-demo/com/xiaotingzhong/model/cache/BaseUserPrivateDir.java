
package com.xiaotingzhong.model.cache;

import com.xiaotingzhong.model.AbsCacheDir;

import android.content.Context;

public abstract class BaseUserPrivateDir extends AbsCacheDir {

    public BaseUserPrivateDir(Context context, long uid) {
        super(context, "" + uid);
    }
}
