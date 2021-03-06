
package org.tadpoleweibo.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<T, V extends View> extends BaseAdapter {
    public static final int INVALID_POSITION = -1;

    protected Activity mContext;

    protected List<T> mList;

    protected V mListView;

    protected int mSelectedPosition = -1;

    public BaseListAdapter(Activity activity) {
        this.mContext = activity;
        mList = new ArrayList<T>();
    }

    protected Context getContext() {
        return this.mContext;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public T getLastItemData() {
        return mList.get(mList.size() - 1);
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

    public void addItem(T item) {
        mList.add(item);
    }

    public void removeItem(T item) {
        mList.remove(item);
    }

    public void has(T item) {
        mList.contains(item);
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

    public void setList(List<T> list) {
        if (list == null) {
            this.mList.clear();
        } else {
            this.mList = list;
        }
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
