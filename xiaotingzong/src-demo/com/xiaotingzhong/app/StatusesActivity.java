package com.xiaotingzhong.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.tadpole.R;
import org.tadpoleweibo.widget.AbsPageListView;
import org.tadpoleweibo.widget.AsyncRoundImageView;
import org.tadpoleweibo.widget.Launcher;
import org.tadpoleweibo.widget.LauncherListAdapter;
import org.tadpoleweibo.widget.PageList;
import org.tadpoleweibo.widget.PageListView;
import org.tadpoleweibo.widget.PageListViewAdapter;
import org.tadpoleweibo.widget.SurfaceImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.WeiboStatuses;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.app.storage.FriendsCacheMgr;
import com.xiaotingzhong.app.storage.SubscriptionMgr;
import com.xiaotingzhong.app.storage.WeiboStatusesCacheMgr;
import com.xiaotingzhong.widget.SubscriptFriendListAdapter;
import com.xiaotingzhong.widget.WeiboStatusesListAdapter;

/**
 * 
 * diplay single user weibo statues
 * 
 * @author chenzh
 * 
 */
public class StatusesActivity extends Activity implements OnRefreshListener2<ListView> {
    static final String TAG = "StatuesActivity";
    static final String USER = "user";

    /**
     * Use Explicit Intent start Activity
     * 
     * @param activity
     * @param uid
     */
    public static void start(Activity activity, User user) {
        Intent intent = new Intent();
        intent.putExtra(USER, user);
        intent.setClass(activity, StatusesActivity.class);
        activity.startActivity(intent);
    }

    private ImageButton mImgBtnLeft = null;
    private ImageButton mImgBtnRight = null;
    private WeiboStatusesListAdapter mPageAdapter = null;
    private PageListView<WeiboStatuses> mListStatuses = null;

    private User mUserSelf = null;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // populate extra
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        mUserSelf = (User) extra.getSerializable(USER);


        setContentView(R.layout.activity_statuses);
        final StatusesActivity me = this;

        this.mImgBtnLeft = ((ImageButton) findViewById(R.id.imgbtn_left));
        this.mImgBtnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                finish();
            }
        });
        this.mImgBtnRight = ((ImageButton) findViewById(R.id.imgbtn_right));
        this.mImgBtnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                finish();
            }
        });

        this.mPageAdapter = new WeiboStatusesListAdapter(this, mUserSelf);
        this.mListStatuses = ((PageListView<WeiboStatuses>) findViewById(R.id.listview_statuses));
        this.mListStatuses.setOnRefreshListener(this);

        this.mListStatuses.setAdapter(this.mPageAdapter);
        mListStatuses.getRefreshableView().setFastScrollEnabled(true);
        mListStatuses.setMode(Mode.BOTH);
        me.fetchStatusesPreferCache(mUserSelf.id);
    }

    /**
     * 
     * fetch friends list from cache. if no cache , fetch from remote
     * 
     * @param uid
     */
    protected void fetchStatusesPreferCache(final long uid) {
        Log.d(TAG, "fetchStatusesPreferCache");
        WeiboStatusesCacheMgr cacheMgr = new WeiboStatusesCacheMgr(this, XTZApplication.app.curUid, uid);
        try {
            ArrayList<WeiboStatuses> list = cacheMgr.getStatusesFromCache();
            if (list != null && (list.size() != 0)) {
                onWeiboStatusesLoad(list, false);
                Log.d(TAG, "fetchStatusesPreferCache use cache");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fetchStatusesFromRemote(uid);
    }

    void fetchStatusesFromRemote(final long uid) {
        Log.d(TAG, "fetchStatusesFromRemote");
        XTZApplication.getStatusesAPI().userTimeline(uid, 0, 0, 30, 1, false, FEATURE.ALL, true, new RequestListener() {
            @Override
            public void onIOException(IOException e) {
            }

            @Override
            public void onError(WeiboException e) {
            }

            @Override
            public void onComplete(String response) {
                try {
                    PageList<WeiboStatuses> pageList = WeiboStatuses.fromUserTimelineJson(response);
                    if (pageList != null) {
                        onWeiboStatusesLoad(pageList.records, false);
                        Log.d("SubscriptionActivity", "loadFromCache");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void fetchStatusesFromRemote(final long uid, final boolean isAdd) {
        Log.d(TAG, "fetchStatusesFromRemote since statuesId = " + mPageAdapter.getLastItemData().id);
        XTZApplication.getStatusesAPI().userTimeline(uid, 0, mPageAdapter.getLastItemData().id - 1, 30, 1, false, FEATURE.ALL, true, new RequestListener() {
            @Override
            public void onIOException(IOException e) {
            }

            @Override
            public void onError(WeiboException e) {
            }

            @Override
            public void onComplete(String response) {
                try {
                    PageList<WeiboStatuses> pageList = WeiboStatuses.fromUserTimelineJson(response);
                    if (pageList != null) {
                        onWeiboStatusesLoad(pageList.records, isAdd);
                        Log.d("SubscriptionActivity", "loadFromCache");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onWeiboStatusesLoad(final ArrayList<WeiboStatuses> list, final boolean isAdd) {
        runOnUiThread(new Runnable() {
            public void run() {
                List<WeiboStatuses> adapterList = mPageAdapter.getList();
                if (isAdd && list != null) {
                    adapterList.addAll(list);
                } else {
                    adapterList = list;
                }
                mPageAdapter.setList(adapterList);
                mPageAdapter.notifyDataSetChanged();
                mListStatuses.onRefreshComplete();
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        fetchStatusesPreferCache(mUserSelf.id);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        fetchStatusesFromRemote(mUserSelf.id, true);
    }
}