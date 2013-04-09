package org.tadpoleweibo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsPageListView<T> extends PullToRefreshListView
  implements AbsListView.OnScrollListener
{
  private static final String TAG = "PageListView";
  private PageListViewAdapter<T> mAdapter;
  private OnLoadPageListListener<T> mLoadPageListener = null;
  private int mNextStartIndex;
  private PageList<T> mPageLst = null;
  private int mTotalCount;
  public int maxResult = 20;

  public AbsPageListView(Context paramContext)
  {
    super(paramContext);
    setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
  }

  public AbsPageListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void addPageList(PageList<T> paramPageList, boolean paramBoolean)
  {
    PageListViewAdapter localPageListViewAdapter = this.mAdapter;
    Log.d("PageListView", "adapter = " + this.mAdapter);
    List localList;
    if (localPageListViewAdapter != null)
    {
      localList = localPageListViewAdapter.getList();
      if (localList != null)
        break label74;
      localPageListViewAdapter.setList(paramPageList.records);
      this.mNextStartIndex = paramPageList.nextStartIndex;
      this.mTotalCount = paramPageList.totalCount;
    }
    while (true)
    {
      localPageListViewAdapter.notifyDataSetChanged();
      return;
      label74: if (!paramBoolean)
      {
        localList.addAll(paramPageList.records);
        this.mNextStartIndex = paramPageList.nextStartIndex;
        this.mTotalCount = paramPageList.totalCount;
        continue;
      }
      ArrayList localArrayList = paramPageList.records;
      localArrayList.removeAll(localList);
      localList.addAll(0, localArrayList);
      this.mTotalCount = paramPageList.totalCount;
      this.mNextStartIndex += localArrayList.size();
    }
  }

  public void doLoad()
  {
    this.mLoadPageListener.onLoadNext(this.mNextStartIndex, this.maxResult);
  }

  public PageList<T> getPageList()
  {
    return this.mPageLst;
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    this.mAdapter = ((PageListViewAdapter)paramListAdapter);
    super.setAdapter(paramListAdapter);
    setMode(PullToRefreshBase.Mode.BOTH);
    setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2()
    {
      public void onPullDownToRefresh(PullToRefreshBase<ListView> paramPullToRefreshBase)
      {
        AbsPageListView.this.mLoadPageListener.onRefreshToGetNew(AbsPageListView.this.maxResult);
      }

      public void onPullUpToRefresh(PullToRefreshBase<ListView> paramPullToRefreshBase)
      {
        Log.d("PageListView", "onPullUpToRefresh mStartItemIndex = " + AbsPageListView.this.mNextStartIndex + ", mTotalCount = " + AbsPageListView.this.mTotalCount);
        if (AbsPageListView.this.mNextStartIndex == 0)
        {
          Toast.makeText(AbsPageListView.this.getContext(), "木有数据啦，不要拉我啦", 1).show();
          paramPullToRefreshBase.postDelayed(new Runnable()
          {
            public void run()
            {
              AbsPageListView.this.onRefreshComplete();
            }
          }
          , 200L);
        }
        while (true)
        {
          return;
          AbsPageListView.this.mLoadPageListener.onLoadNext(AbsPageListView.this.mNextStartIndex, AbsPageListView.this.maxResult);
        }
      }
    });
  }

  public void setOnLoadPageListListener(OnLoadPageListListener<T> paramOnLoadPageListListener)
  {
    this.mLoadPageListener = paramOnLoadPageListListener;
  }

  public void setPageList(PageList<T> paramPageList)
  {
    PageListViewAdapter localPageListViewAdapter = this.mAdapter;
    if (localPageListViewAdapter != null)
    {
      localPageListViewAdapter.setList(paramPageList.records);
      this.mTotalCount = paramPageList.totalCount;
      this.mNextStartIndex += paramPageList.records.size();
      localPageListViewAdapter.notifyDataSetChanged();
    }
  }

  public static abstract interface OnLoadPageListListener<T>
  {
    public abstract PageList<T> onLoadNext(int paramInt1, int paramInt2);

    public abstract PageList<T> onRefreshToGetNew(int paramInt);
  }
}