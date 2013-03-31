package org.tadpoleweibo.widget;

import java.util.Collections;
import java.util.List;

public abstract class LancherListAdapter<T> extends LancherAdapter {

    private List<T> mData = null;

    public LancherListAdapter(List<T> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public T getData(int position) {
        return mData.get(position);
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onSwapPosition(int from, int to) {
        Collections.swap(mData, from, to);
        notifyDataSetChanged();
    }

    @Override
    public void onDelete(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
}
