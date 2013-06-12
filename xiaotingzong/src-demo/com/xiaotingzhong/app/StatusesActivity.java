
package com.xiaotingzhong.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.tadpole.R;
import org.tadpoleweibo.framework.TpNavigationActivity;
import org.tadpoleweibo.widget.PageList;
import org.tadpoleweibo.widget.PageListView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
import com.xiaotingzhong.model.state.UserToCurrUserShip;
import com.xiaotingzhong.widget.WeiboStatusesListAdapter;

/**
 * diplay single user weibo statues
 * 
 * @author chenzh
 */
public class StatusesActivity extends TpNavigationActivity implements OnRefreshListener2<ListView>,
        OnItemClickListener {
    static final String TAG = "StatuesActivity";

    static final String EXTRA_USER = "user";

    static final String EXTRA_USER_TO_CURR_USER_SHIP = "userState";

    /**
     * Use Explicit Intent start Activity
     * 
     * @param activity
     * @param user 微博列表所属用户
     * @param userState
     */
    public static void start(Context activity, User user) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_USER_TO_CURR_USER_SHIP, XTZApplication.getUserToCurrUserShip(user));
        intent.setClass(activity, StatusesActivity.class);
        activity.startActivity(intent);
    }

    private WeiboStatusesListAdapter mListAdapter = null;

    private PageListView<WeiboStatus> mListStatuses = null;

    private User mUserSelf = null;

    private UserToCurrUserShip mUserToCurrUserShip = null;

    // state
    private boolean mHasSubscriptChange = false;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // populate extra
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        mUserSelf = (User)extra.getSerializable(EXTRA_USER);
        mUserToCurrUserShip = (UserToCurrUserShip)extra
                .getSerializable(EXTRA_USER_TO_CURR_USER_SHIP);

        setContentView(R.layout.activity_statuses);

        getNavBar().setTitle(mUserSelf.screen_name);
        getNavBar().setListener(new NavBarListener() {
            @Override
            public void onDefaultLeftBtnClick(ITpNavBar navBar, View v) {
                finish();
            }

            @Override
            public void onDefaultRightBtnClick(ITpNavBar navBar, View v) {
                mHasSubscriptChange = true;

                Log.d(TAG, " getCurUser = " + XTZApplication.getCurUser());

                if (mUserToCurrUserShip.hasSubscript) {
                    XTZApplication.getCurUser().unSubscript(mUserSelf);
                } else {
                    XTZApplication.getCurUser().subscript(mUserSelf);
                }
                mUserToCurrUserShip.hasSubscript = !mUserToCurrUserShip.hasSubscript;
                updateImgBtnRightImage();
            }

        });

        if (mUserSelf.id == XTZApplication.getCurUser().id) {
            getNavBar().getBtnRight().setVisibility(View.INVISIBLE);
        }
        updateImgBtnRightImage();

        this.mListAdapter = new WeiboStatusesListAdapter(this, mUserSelf);
        this.mListStatuses = ((PageListView<WeiboStatus>)findViewById(R.id.listview_statuses));
        this.mListStatuses.setOnRefreshListener(this);

        this.mListStatuses.setAdapter(this.mListAdapter);

        //
        ListView listView = mListStatuses.getRefreshableView();
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(this);

        mListStatuses.setMode(Mode.BOTH);
        mListStatuses.firePullDownToRefresh();
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
        if (mUserToCurrUserShip.hasSubscript) {
            getNavBar().getBtnRight().setImageResource(
                    R.drawable.selector_rootblock_add_toolbar_added);
        } else {
            getNavBar().getBtnRight().setImageResource(R.drawable.selector_icon_add);
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
        Log.d(TAG, "fetchStatusesFromRemote since statuesId = " + mListAdapter.getLastItemData().id);
        XTZApplication.getStatusesAPI().userTimeline(uid, 0, mListAdapter.getLastItemData().id - 1,
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

    private Comparator<WeiboStatus> mComparator = new Comparator<WeiboStatus>() {

        @Override
        public int compare(WeiboStatus lhs, WeiboStatus rhs) {
            if (lhs.getCreateAtLong() >= rhs.getCreateAtLong()) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    public void onWeiboStatusesLoad(final ArrayList<WeiboStatus> list, final boolean isAdd) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<WeiboStatus> adapterList = mListAdapter.getList();
                if (isAdd && list != null) {
                    adapterList.addAll(list);
                } else {
                    adapterList = list;
                }
                Collections.sort(adapterList, mComparator);
                mListAdapter.setList(adapterList);
                mListAdapter.notifyDataSetChanged();
                mListStatuses.proxyOnRefreshComplete();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private Handler mHandler = new Handler();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListAdapter == null) {
            return;
        }
    }

}
