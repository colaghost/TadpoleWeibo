package org.tadpoleweibo.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

public abstract class BaseListAdapter<T, V extends View> extends BaseAdapter
{
  public static final int INVALID_POSITION = -1;
  protected Activity mContext;
  protected List<T> mList;
  protected V mListView;
  protected int mSelectedPosition = -1;

  public BaseListAdapter(Activity paramActivity)
  {
    this.mContext = paramActivity;
  }

  protected Context getContext()
  {
    return this.mContext;
  }

  public int getCount()
  {
    if (this.mList != null);
    for (int i = this.mList.size(); ; i = 0)
      return i;
  }

  public Object getItem(int paramInt)
  {
    if (this.mList == null);
    for (Object localObject = null; ; localObject = this.mList.get(paramInt))
      return localObject;
  }

  public T getItemData(int paramInt)
  {
    if (this.mList == null);
    for (Object localObject = null; ; localObject = this.mList.get(paramInt))
      return localObject;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  protected LayoutInflater getLayoutInflater()
  {
    return LayoutInflater.from(this.mContext);
  }

  public List<T> getList()
  {
    return this.mList;
  }

  public V getListView()
  {
    return this.mListView;
  }

  public int getSelectedPostion()
  {
    return this.mSelectedPosition;
  }

  public abstract View getView(int paramInt, View paramView, ViewGroup paramViewGroup);

  public void setList(List<T> paramList)
  {
    this.mList = paramList;
    notifyDataSetChanged();
  }

  public void setListView(V paramV)
  {
    this.mListView = paramV;
  }

  public void setSelectedPostion(int paramInt)
  {
    if (this.mSelectedPosition != paramInt)
    {
      this.mSelectedPosition = paramInt;
      notifyDataSetChanged();
    }
  }
}