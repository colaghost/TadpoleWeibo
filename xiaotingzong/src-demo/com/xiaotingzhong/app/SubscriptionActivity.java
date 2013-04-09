package com.xiaotingzhong.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tadpole.R;
import org.tadpoleweibo.widget.AbsPageListView;
import org.tadpoleweibo.widget.PageList;
import org.tadpoleweibo.widget.PageListView;
import org.tadpoleweibo.widget.PageListViewAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.ApiFactory;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.app.storage.FriendsCacheMgr;
import com.xiaotingzhong.widget.SubscriptFriendListAdapter;

public class SubscriptionActivity extends Activity {
    static final String TAG = "SubscriptionActivity";
    private EditText mEditTxtSearch = null;
    private PageList<User> mFriendsPageListTotal = null;
    private ImageButton mImgBtnLeft = null;
    private ImageButton mImgBtnRight = null;
    private PageListViewAdapter<User> mPageAdapter = null;
    private PageListView<User> mListFriends = null;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_subscription);
        final SubscriptionActivity me = this;

        this.mImgBtnLeft = ((ImageButton) findViewById(R.id.imgbtn_left));
        this.mImgBtnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                SubscriptionActivity.this.finish();
            }
        });
        this.mImgBtnRight = ((ImageButton) findViewById(R.id.imgbtn_right));
        this.mImgBtnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                SubscriptionActivity.this.finish();
            }
        });
        this.mEditTxtSearch = ((EditText) findViewById(R.id.edittext_search));
        this.mEditTxtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SubscriptionActivity.this.searchFriend(new StringBuilder(s).toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.mPageAdapter = new SubscriptFriendListAdapter(this);
        this.mListFriends = ((PageListView<User>) findViewById(R.id.listview_friends));
        this.mListFriends.setOnLoadPageListListener(new AbsPageListView.OnLoadPageListListener() {
            public PageList<User> onLoadNext(int startItemIndex, int maxResult) {
                Log.d("SubscriptionActivity", "mPageListViewWeibo onLoadNext startItemIndex = " + startItemIndex);
                me.fetchFriendsPreferCache(XTZApplication.app.curUid);
                return null;
            }

            public PageList<User> onRefreshToGetNew(int maxResult) {
                Log.d("SubscriptionActivity", "mPageListViewWeibo onRefreshToGetNew");
                me.loadFriendsFromRemote();
                return null;
            }
        });
        this.mListFriends.setAdapter(this.mPageAdapter);


        // TODO 将这些设置转移到xml配置文件。
        ListView listView = (ListView) this.mListFriends.getRefreshableView();
        listView.setDividerHeight(1);
        //        listView.setDivider(getResources().getDrawable(R.drawable.divider));
        listView.setVerticalScrollBarEnabled(false);

        this.mListFriends.doLoad();
    }

    /**
     * 
     * fetch friends list from cache. if no cache , fetch from remote
     * 
     * @param uid
     */
    protected void fetchFriendsPreferCache(final int uid) {
        final SubscriptionActivity me = this;
        new Thread(new Runnable() {
            public void run() {
                FriendsCacheMgr friendMgr = new FriendsCacheMgr(me, uid);
                try {
                    PageList<User> pageList = friendMgr.getFriendsFromCache();
                    if (pageList != null) {
                        SubscriptionActivity.this.onFriendListLoad(pageList, false);
                        Log.d("SubscriptionActivity", "loadFromCache");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                loadFriendsFromRemote();
            }
        }).start();
    }

    public void loadFriendsFromRemote() {
        Log.d("SubscriptionActivity", "loadFromRemote ");
        ApiFactory.getFriendShipsAPI(this).friends(XTZApplication.app.curUid, 200, 0, true, new RequestListener() {
            public void onComplete(String response) {
                try {
                    final PageList pageList = new FriendsCacheMgr(SubscriptionActivity.this.getApplicationContext(), XTZApplication.app.curUid).saveAndGetFriends(response);
                    Log.d("SubscriptionActivity", "pageList.size = " + pageList.records.size());
                    SubscriptionActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            SubscriptionActivity.this.onFriendListLoad(pageList, false);
                        }
                    });
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onError(WeiboException we) {
                SubscriptionActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        SubscriptionActivity.this.mListFriends.onRefreshComplete();
                    }
                });
            }

            public void onIOException(IOException e) {
                SubscriptionActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        SubscriptionActivity.this.mListFriends.onRefreshComplete();
                    }
                });
            }
        });
    }


    public void onFriendListLoad(final PageList<User> pageList, final boolean isSearch) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (isSearch) {
                    SubscriptionActivity.this.mListFriends.setPullToRefreshOverScrollEnabled(false);
                } else {
                    SubscriptionActivity.this.mFriendsPageListTotal = pageList;
                }
                while (true) {
                    SubscriptionActivity.this.mListFriends.setPageList(pageList);
                    SubscriptionActivity.this.mListFriends.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(SubscriptionActivity.this, R.anim.list_anim_layout));
                    SubscriptionActivity.this.mListFriends.onRefreshComplete();
                    SubscriptionActivity.this.mListFriends.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    return;
                }
            }
        });
    }

    public void searchFriend(String str) {
        Log.d("SubscriptionActivity", "searchFriend searchKey = " + str);
        PageList<User> pageList;
        if ((str == null) || ("".equals(str))) {
            pageList = this.mFriendsPageListTotal;
            this.mListFriends.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            onFriendListLoad(pageList, true);
            return;
        }


        this.mListFriends.setMode(PullToRefreshBase.Mode.DISABLED);
        List<User> list = this.mPageAdapter.getList();
        ArrayList<User> newList = new ArrayList<User>();
        for (User user : list) {
            if (user.screen_name.toLowerCase().indexOf(str.toLowerCase()) != -1) {
                newList.add(user);
            }
        }
        pageList = new PageList<User>();
        pageList.records = newList;
        pageList.totalCount = list.size();
        onFriendListLoad(pageList, true);
    }
}