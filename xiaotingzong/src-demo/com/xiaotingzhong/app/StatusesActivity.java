
package com.xiaotingzhong.app;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.broadcast.SubscriptReceiver;
import com.xiaotingzhong.model.User;
import com.xiaotingzhong.model.WeiboStatus;
import com.xiaotingzhong.model.cache.userprivate.StatusesCache;
import com.xiaotingzhong.model.state.UserState;
import com.xiaotingzhong.widget.WeiboStatusesListAdapter;

import org.tadpole.R;
import org.tadpoleweibo.widget.PageList;
import org.tadpoleweibo.widget.PageListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * diplay single user weibo statues
 * 
 * @author chenzh
 */
public class StatusesActivity extends Activity implements OnRefreshListener2<ListView> {
    static final String TAG = "StatuesActivity";

    static final String EXTRA_USER = "user";

    static final String EXTRA_USER_STATE = "userState";

    /**
     * Use Explicit Intent start Activity
     * 
     * @param activity
     * @param user
     * @param
     */
    public static void start(Context activity, User user, UserState userState) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_USER_STATE, userState);
        intent.setClass(activity, StatusesActivity.class);
        activity.startActivity(intent);
    }

    private ImageButton mImgBtnLeft = null;

    private ImageButton mImgBtnRight = null;

    private WeiboStatusesListAdapter mPageAdapter = null;

    private PageListView<WeiboStatus> mListStatuses = null;

    private User mUserSelf = null;

    private UserState mUserState = null;

    // state
    private boolean mHasSubscriptChange = false;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // populate extra
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        mUserSelf = (User)extra.getSerializable(EXTRA_USER);
        mUserState = (UserState)extra.getSerializable(EXTRA_USER_STATE);

        setContentView(R.layout.activity_statuses);
        final StatusesActivity me = this;

        this.mImgBtnLeft = ((ImageButton)findViewById(R.id.imgbtn_left));
        this.mImgBtnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                finish();
            }
        });

        this.mImgBtnRight = ((ImageButton)findViewById(R.id.imgbtn_right));
        this.mImgBtnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                mHasSubscriptChange = true;
                if (mUserState.hasSubscript) {
                    XTZApplication.getCurUser().unSubscript(mUserSelf);
                } else {
                    XTZApplication.getCurUser().subscript(mUserSelf);
                }
                mUserState.hasSubscript = !mUserState.hasSubscript;
                updateImgBtnRightImage();
            }
        });
        updateImgBtnRightImage();

        this.mPageAdapter = new WeiboStatusesListAdapter(this, mUserSelf);
        this.mListStatuses = ((PageListView<WeiboStatus>)findViewById(R.id.listview_statuses));
        this.mListStatuses.setOnRefreshListener(this);

        this.mListStatuses.setAdapter(this.mPageAdapter);
        mListStatuses.getRefreshableView().setFastScrollEnabled(true);
        mListStatuses.setMode(Mode.BOTH);
        me.fetchStatusesPreferCache(mUserSelf.id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHasSubscriptChange) {
            mHasSubscriptChange = false;
            SubscriptReceiver.sendBroadCast(this.getApplicationContext());
        }
    }

    public void updateImgBtnRightImage() {
        // 根据是否好友显示右边按钮的内容
        if (mUserState.hasSubscript) {
            this.mImgBtnRight.setImageResource(R.drawable.selector_rootblock_add_toolbar_added);
        } else {
            this.mImgBtnRight.setImageResource(R.drawable.selector_icon_add);
        }
    }

    /**
     * fetch friends list from cache. if no cache , fetch from remote
     * 
     * @param uid
     */
    protected void fetchStatusesPreferCache(final long uid) {
        Log.d(TAG, "fetchStatusesPreferCache");
        StatusesCache cacheMgr = new StatusesCache(this, XTZApplication.getCurUid(), uid);
        try {
            ArrayList<WeiboStatus> list = cacheMgr.getStatusesFromCache();
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
        XTZApplication.getStatusesAPI().userTimeline(uid, 0, 0, 30, 1, false, FEATURE.ALL, false,
                new RequestListener() {
                    @Override
                    public void onIOException(IOException e) {
                    }

                    @Override
                    public void onError(WeiboException e) {
                    }

                    @Override
                    public void onComplete(String response) {
                        StatusesCache cacheMgr = new StatusesCache(StatusesActivity.this,
                                XTZApplication.getCurUid(), uid);
                        try {
                            PageList<WeiboStatus> pageList = cacheMgr.saveAndGetStatuses(response);
                            if (pageList != null) {
                                onWeiboStatusesLoad(pageList.records, false);
                                Log.d("SubscriptionActivity", "loadFromCache");
                                return;
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    void fetchStatusesFromRemote(final long uid, final boolean isAdd) {
        Log.d(TAG, "fetchStatusesFromRemote since statuesId = " + mPageAdapter.getLastItemData().id);
        XTZApplication.getStatusesAPI().userTimeline(uid, 0, mPageAdapter.getLastItemData().id - 1,
                30, 1, false, FEATURE.ALL, false, new RequestListener() {
                    @Override
                    public void onIOException(IOException e) {
                    }

                    @Override
                    public void onError(WeiboException e) {
                    }

                    @Override
                    public void onComplete(String response) {
                        StatusesCache cacheMgr = new StatusesCache(StatusesActivity.this,
                                XTZApplication.getCurUid(), uid);
                        try {
                            PageList<WeiboStatus> pageList = cacheMgr.saveAndGetStatuses(response);
                            if (pageList != null) {
                                onWeiboStatusesLoad(pageList.records, isAdd);
                                Log.d("SubscriptionActivity", "loadFromCache");
                                return;
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void onWeiboStatusesLoad(final ArrayList<WeiboStatus> list, final boolean isAdd) {
        runOnUiThread(new Runnable() {
            public void run() {
                List<WeiboStatus> adapterList = mPageAdapter.getList();
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
