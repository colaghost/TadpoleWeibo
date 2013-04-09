package org.tadpoleweibo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;

public class PageListView<T> extends AbsPageListView<T>
{
  private AbsPageListView.OnLoadPageListListener<T> mLoadPageListener = null;

  public PageListView(Context paramContext)
  {
    super(paramContext);
  }

  public PageListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    super.setAdapter(paramListAdapter);
  }
}