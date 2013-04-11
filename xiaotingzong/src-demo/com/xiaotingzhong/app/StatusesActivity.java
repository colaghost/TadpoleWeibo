package com.xiaotingzhong.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.WeiboStatuses;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.app.storage.FriendsCacheMgr;
import com.xiaotingzhong.app.storage.SubscriptionMgr;
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

        this.mPageAdapter = new WeiboStatusesListAdapter(this);
        this.mListStatuses = ((PageListView<WeiboStatuses>) findViewById(R.id.listview_statuses));
        this.mListStatuses.setOnRefreshListener(this);

        me.fetchStatusesPreferCache(mUserSelf.id);
        this.mListStatuses.setAdapter(this.mPageAdapter);
        
        mListStatuses.getRefreshableView().setFastScrollEnabled(true);
    }

    /**
     * 
     * fetch friends list from cache. if no cache , fetch from remote
     * 
     * @param uid
     */
    protected void fetchStatusesPreferCache(final int uid) {
        final StatusesActivity me = this;
        XTZApplication.getStatusesAPI().userTimeline(0, 0, 100, 0, false, FEATURE.ALL, true, new RequestListener() {
            @Override
            public void onIOException(IOException e) {
            }
            @Override
            public void onError(WeiboException e) {
            }
            @Override
            public void onComplete(String response) {
            }
        });
        
        
        new Thread(new Runnable() {
            public void run() {
                try {
                    ArrayList<WeiboStatuses> list = new ArrayList<WeiboStatuses>();
                    for (int i = 0, len = 100; i < len; i++) {
                        list.add(new WeiboStatuses());
                    }
                    PageList<WeiboStatuses> pageList = new PageList<WeiboStatuses>();
                    pageList.records = list;
                    pageList.totalCount = list.size();

                    if (pageList != null) {
                        onWeiboStatusesLoad(pageList);
                        Log.d("SubscriptionActivity", "loadFromCache");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onWeiboStatusesLoad(final PageList<WeiboStatuses> pageList) {
        runOnUiThread(new Runnable() {
            public void run() {
                mPageAdapter.setList(pageList.records);
                mPageAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        fetchStatusesPreferCache(mUserSelf.id);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }
}