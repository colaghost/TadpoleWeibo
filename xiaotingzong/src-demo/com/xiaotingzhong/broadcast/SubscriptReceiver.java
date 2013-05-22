
package com.xiaotingzhong.broadcast;

import com.xiaotingzhong.app.LauncherActivity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * regiseter by LauncherActivity <br>=
 * ========================= <br>
 * author：Zenip <br>
 * email：lxyczh@gmail.com <br>
 * create：2013-4-30 <br>=
 * =========================
 */
public class SubscriptReceiver extends BroadcastReceiver {

    static final String ACTION = "com.xiaotingzhong.broadcast.SubscriptReceiver";

    static final IntentFilter INTENT_FILTER = new IntentFilter(ACTION);

    static final Intent INTENT = new Intent(ACTION);

    private LauncherActivity mActivity;

    public static void sendBroadCast(Context context) {
        context.sendBroadcast(INTENT);
    }

    public void register() {
        mActivity.registerReceiver(this, INTENT_FILTER);
    }

    public void unRegister() {
        mActivity.unregisterReceiver(this);
    }

    public SubscriptReceiver(LauncherActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("SubscriptReceiver = " + intent.getAction());
        mActivity.fetchUserFriends();
    }
}
