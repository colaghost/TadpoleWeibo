package org.tadpoleweibo.widget;

import android.app.Activity;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public abstract class PageListViewAdapter<T> extends BaseListAdapter<T, PullToRefreshListView>
{
  public PageListViewAdapter(Activity paramActivity)
  {
    super(paramActivity);
  }
}