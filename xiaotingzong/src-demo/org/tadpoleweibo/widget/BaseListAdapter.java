package org.tadpoleweibo.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

public abstract class BaseListAdapter<T, V extends View> extends BaseAdapter {
    public static final int INVALID_POSITION = -1;
    protected Activity mContext;
    protected List<T> mList;
    protected V mListView;
    protected int mSelectedPosition = -1;

    public BaseListAdapter(Activity paramActivity) {
        this.mContext = paramActivity;
    }

    protected Context getContext() {
        return this.mContext;
    }

    public int getCount() {
        if (this.mList == null) {
            return 0;
        }
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public T getItemData(int position) {
        if (this.mList == null) {
            return null;
        }
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(this.mContext);
    }

    public List<T> getList() {
        return this.mList;
    }

    public V getListView() {
        return this.mListView;
    }

    public int getSelectedPostion() {
        return this.mSelectedPosition;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

    public void setList(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void setListView(V view) {
        this.mListView = view;
    }

    public void setSelectedPostion(int postion) {
        if (this.mSelectedPosition != postion) {
            this.mSelectedPosition = postion;
            notifyDataSetChanged();
        }
    }
}