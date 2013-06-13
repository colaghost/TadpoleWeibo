
package com.xiaotingzhong.app;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.app.window.FriendsWindow;
import com.xiaotingzhong.model.User;
import com.xiaotingzhong.model.cache.userprivate.FriendsCache;
import com.xiaotingzhong.widget.SubscriptFriendListAdapter;

import org.tadpole.R;
import org.tadpoleweibo.framework.NavgationActivity;
import org.tadpoleweibo.widget.PageList;
import org.tadpoleweibo.widget.PageListView;
import org.tadpoleweibo.widget.PageListViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionActivity extends NavgationActivity {
    static final String TAG = "SubscriptionActivity";

    static final String USER = "user";

    /**
     * Use Explicit Intent start Activity
     * 
     * @param activity
     * @param uid
     */
    public static void start(Activity activity, User user) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(USER, user);
        intent.setClass(activity, SubscriptionActivity.class);
        activity.startActivity(intent);
    }

    public static void startForResult(Activity activity, User user, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(USER, user);
        intent.setClass(activity, SubscriptionActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        pushWindow(new FriendsWindow());
    }
}
