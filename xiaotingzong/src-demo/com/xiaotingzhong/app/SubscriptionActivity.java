package com.xiaotingzhong.app;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.ApiFactory;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.app.storage.FriendsCacheMgr;
import com.xiaotingzhong.app.storage.SubscriptionMgr;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tadpoleweibo.widget.AbsPageListView.OnLoadPageListListener;
import org.tadpoleweibo.widget.AsyncImageView;
import org.tadpoleweibo.widget.PageList;
import org.tadpoleweibo.widget.PageListView;
import org.tadpoleweibo.widget.PageListViewAdapter;

public class SubscriptionActivity extends Activity
{
  static final String TAG = "SubscriptionActivity";
  private EditText mEditTxtSearch = null;
  private PageList<User> mFriendsPageListTotal = null;
  private ImageButton mImgBtnLeft = null;
  private ImageButton mImgBtnRight = null;
  private PageListViewAdapter<User> mPageAdapter = null;
  private PageListView<User> mPageListViewWeibo = null;

  protected void fetchWeiboListPreferCache(int paramInt)
  {
    new Thread(new Runnable(this, paramInt)
    {
      public void run()
      {
        FriendsCacheMgr localFriendsCacheMgr = new FriendsCacheMgr(this.val$me, this.val$uid);
        try
        {
          PageList localPageList = localFriendsCacheMgr.getFriendsFromCache();
          SubscriptionActivity.this.onFriendListLoad(localPageList, false);
          i = 1;
          if (i != 0)
          {
            Log.d("SubscriptionActivity", "loadFromCache");
            return;
          }
        }
        catch (Exception localException)
        {
          while (true)
          {
            localException.printStackTrace();
            int i = 0;
            continue;
            SubscriptionActivity.this.loadFromRemote();
          }
        }
      }
    }).start();
  }

  public void loadFromRemote()
  {
    Log.d("SubscriptionActivity", "loadFromRemote ");
    ApiFactory.getFriendShipsAPI(this).friends(XTZApplication.curUid, 200, 0, true, new RequestListener()
    {
      public void onComplete(String paramString)
      {
        try
        {
          PageList localPageList = new FriendsCacheMgr(SubscriptionActivity.this.getApplicationContext(), XTZApplication.curUid).saveAndGetFriends(paramString);
          Log.d("SubscriptionActivity", "pageList.size = " + localPageList.records.size());
          SubscriptionActivity.this.runOnUiThread(new Runnable(localPageList)
          {
            public void run()
            {
              SubscriptionActivity.this.onFriendListLoad(this.val$pageList, false);
            }
          });
          return;
        }
        catch (Exception localException)
        {
          while (true)
            localException.printStackTrace();
        }
      }

      public void onError(WeiboException paramWeiboException)
      {
        SubscriptionActivity.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            SubscriptionActivity.this.mPageListViewWeibo.onRefreshComplete();
          }
        });
      }

      public void onIOException(IOException paramIOException)
      {
        SubscriptionActivity.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            SubscriptionActivity.this.mPageListViewWeibo.onRefreshComplete();
          }
        });
      }
    });
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().setFlags(16777216, 16777216);
    setContentView(2130903042);
    this.mImgBtnLeft = ((ImageButton)findViewById(2131034132));
    this.mImgBtnLeft.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SubscriptionActivity.this.finish();
      }
    });
    this.mImgBtnRight = ((ImageButton)findViewById(2131034133));
    this.mImgBtnRight.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SubscriptionActivity.this.finish();
      }
    });
    this.mEditTxtSearch = ((EditText)findViewById(2131034134));
    this.mEditTxtSearch.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
        SubscriptionActivity.this.searchFriend(paramCharSequence);
      }
    });
    this.mPageAdapter = new PageListViewAdapter(this, new SubscriptionMgr(this, XTZApplication.curUid))
    {
      public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
      {
        User localUser = (User)getItemData(paramInt);
        View localView = LayoutInflater.from(getContext()).inflate(2130903044, null);
        AsyncImageView localAsyncImageView = (AsyncImageView)localView.findViewById(2131034140);
        TextView localTextView = (TextView)localView.findViewById(2131034141);
        ImageButton localImageButton = (ImageButton)localView.findViewById(2131034127);
        localAsyncImageView.setImageURL(localUser.profile_image_url);
        localTextView.setText(localUser.screen_name);
        if (this.val$subscriptMgr.isSubscripted(localUser.id))
          localImageButton.setImageResource(2130837532);
        while (true)
        {
          localImageButton.setOnClickListener(new View.OnClickListener(this.val$subscriptMgr, localUser)
          {
            public void onClick(View paramView)
            {
              if (this.val$subscriptMgr.isSubscripted(this.val$user.id))
              {
                this.val$subscriptMgr.unSubscript(this.val$user.id);
                ((ImageButton)paramView).setImageResource(2130837524);
              }
              while (true)
              {
                return;
                this.val$subscriptMgr.subscript(this.val$user.id);
                ((ImageButton)paramView).setImageResource(2130837532);
              }
            }
          });
          return localView;
          localImageButton.setImageResource(2130837524);
        }
      }
    };
    this.mPageListViewWeibo = ((PageListView)findViewById(2131034135));
    this.mPageListViewWeibo.setOnLoadPageListListener(new AbsPageListView.OnLoadPageListListener(this)
    {
      public PageList<User> onLoadNext(int paramInt1, int paramInt2)
      {
        Log.d("SubscriptionActivity", "mPageListViewWeibo onLoadNext startItemIndex = " + paramInt1);
        this.val$me.fetchWeiboListPreferCache(XTZApplication.curUid);
        return null;
      }

      public PageList<User> onRefreshToGetNew(int paramInt)
      {
        Log.d("SubscriptionActivity", "mPageListViewWeibo onRefreshToGetNew");
        this.val$me.loadFromRemote();
        return null;
      }
    });
    this.mPageListViewWeibo.setAdapter(this.mPageAdapter);
    ListView localListView = (ListView)this.mPageListViewWeibo.getRefreshableView();
    localListView.setDividerHeight(1);
    localListView.setDivider(getResources().getDrawable(2130837520));
    localListView.setVerticalScrollBarEnabled(false);
    localListView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, 2130968578));
    this.mPageListViewWeibo.doLoad();
  }

  public void onFriendListLoad(PageList<User> paramPageList, boolean paramBoolean)
  {
    runOnUiThread(new Runnable(paramBoolean, paramPageList)
    {
      public void run()
      {
        if (this.val$isSearch)
          SubscriptionActivity.this.mPageListViewWeibo.setPullToRefreshOverScrollEnabled(false);
        while (true)
        {
          SubscriptionActivity.this.mPageListViewWeibo.setPageList(this.val$pageList);
          SubscriptionActivity.this.mPageListViewWeibo.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(SubscriptionActivity.this, 2130968578));
          SubscriptionActivity.this.mPageListViewWeibo.onRefreshComplete();
          SubscriptionActivity.this.mPageListViewWeibo.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
          return;
          SubscriptionActivity.this.mFriendsPageListTotal = this.val$pageList;
        }
      }
    });
  }

  public void searchFriend(String paramString)
  {
    Log.d("SubscriptionActivity", "searchFriend searchKey = " + paramString);
    PageList localPageList;
    if ((paramString == null) || ("".equals(paramString)))
    {
      localPageList = this.mFriendsPageListTotal;
      this.mPageListViewWeibo.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
      onFriendListLoad(localPageList, true);
      return;
    }
    this.mPageListViewWeibo.setMode(PullToRefreshBase.Mode.DISABLED);
    List localList = this.mPageAdapter.getList();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = localList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
      {
        localPageList = new PageList();
        localPageList.records = localArrayList;
        localPageList.totalCount = localArrayList.size();
        break;
      }
      User localUser = (User)localIterator.next();
      if (localUser.screen_name.toLowerCase().indexOf(paramString.toLowerCase()) == -1)
        continue;
      localArrayList.add(localUser);
    }
  }
}