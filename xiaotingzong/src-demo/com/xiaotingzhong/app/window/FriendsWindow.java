
package com.xiaotingzhong.app.window;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.app.StatusesActivity;
import com.xiaotingzhong.app.SubscriptionActivity;
import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.model.User;
import com.xiaotingzhong.model.cache.userprivate.FriendsCache;
import com.xiaotingzhong.widget.SubscriptFriendListAdapter;

import org.tadpole.R;
import org.tadpoleweibo.framework.AbstractTpWindow;
import org.tadpoleweibo.widget.PageList;
import org.tadpoleweibo.widget.PageListView;
import org.tadpoleweibo.widget.PageListViewAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendsWindow extends AbstractTpWindow implements OnItemClickListener {

    private static final String TAG = "FriendsWindow";

    static final String USER = "user";

    private EditText mEditTxtSearch = null;

    private ArrayList<User> mListFriendsAll = null;

    private PageListViewAdapter<User> mAdapterFriends = null;

    private PageListView<User> mListViewFriends = null;

    private User mUserSelf = null;

    @Override
    public View onCreate() {
        // populate extra
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        mUserSelf = (User)extra.getSerializable(USER);

        View view = getLayoutInflater().inflate(R.layout.activity_subscription, null);

        getNavBar().setTitle("好友列表");
        getNavBar().getBtnRight().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pushWindow(new HelloWindow());
            }
        });

        this.mEditTxtSearch = ((EditText)view.findViewById(R.id.edittext_search));
        this.mEditTxtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFriend(new StringBuilder(s).toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.mAdapterFriends = new SubscriptFriendListAdapter(getActivity(), mUserSelf);
        this.mListViewFriends = ((PageListView<User>)view.findViewById(R.id.listview_friends));
        this.mListViewFriends.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                fetchFriendsPreferCache(mUserSelf.id);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
        this.mListViewFriends.setAdapter(this.mAdapterFriends);
        mListViewFriends.setOnItemClickListener(this);

        // TODO 将这些设置转移到xml配置文件。
        ListView listView = (ListView)this.mListViewFriends.getRefreshableView();
        listView.setDivider(getResources().getDrawable(R.drawable.divider));
        listView.setVerticalScrollBarEnabled(false);
        listView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mListViewFriends.firePullDownToRefresh();
        return view;
    }

    /**
     * fetch friends list from cache. if no cache , fetch from remote
     * 
     * @param uid
     */
    protected void fetchFriendsPreferCache(final long uid) {
        new Thread(new Runnable() {
            public void run() {
                FriendsCache friendMgr = new FriendsCache(getActivity(), uid);
                try {
                    ArrayList<User> list = friendMgr.getFriendsFromCache();
                    if (list != null) {
                        onFriendListLoad(list, false);
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
        XTZApplication.getFriendshipsAPI().friends(mUserSelf.id, 200, 0, true,
                new RequestListener() {
                    public void onComplete(String response) {
                        try {
                            final PageList pageList = new FriendsCache(getActivity(), mUserSelf.id)
                                    .saveAndGetFriends(response);
                            Log.d("SubscriptionActivity",
                                    "pageList.size = " + pageList.records.size());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    onFriendListLoad(pageList.records, false);
                                }
                            });
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onError(WeiboException we) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mListViewFriends.proxyOnRefreshComplete();
                            }
                        });
                    }

                    public void onIOException(IOException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mListViewFriends.proxyOnRefreshComplete();
                            }
                        });
                    }
                });
    }

    public void onFriendListLoad(final ArrayList<User> list, final boolean isSearch) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (isSearch) {
                    mListViewFriends.setPullToRefreshOverScrollEnabled(false);
                } else {
                    mListFriendsAll = list;
                }
                mAdapterFriends.setList(list);
                mListViewFriends.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(
                        getActivity(), R.anim.list_anim_layout));
                mListViewFriends.proxyOnRefreshComplete();
                mListViewFriends.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        });
    }

    public void searchFriend(String str) {
        Log.d("SubscriptionActivity", "searchFriend searchKey = " + str);
        ArrayList<User> userList;
        if ((str == null) || ("".equals(str))) {
            userList = this.mListFriendsAll;
            this.mListViewFriends.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            onFriendListLoad(userList, true);
            return;
        }
        this.mListViewFriends.setMode(PullToRefreshBase.Mode.DISABLED);
        List<User> list = this.mAdapterFriends.getList();
        ArrayList<User> newList = new ArrayList<User>();
        for (User user : list) {
            if (user.screen_name.toLowerCase().indexOf(str.toLowerCase()) != -1) {
                newList.add(user);
            }
        }
        onFriendListLoad(newList, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick");
        if (mAdapterFriends == null) {
            return;
        }

        // has HeaderView
        position = position - 1;

        User user = mAdapterFriends.getItemData(position);
        StatusesActivity.start(getActivity(), user);
    }

}
