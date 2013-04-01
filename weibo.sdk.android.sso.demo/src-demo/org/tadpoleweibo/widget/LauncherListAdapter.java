package org.tadpoleweibo.widget;

import java.util.List;

import org.tadpoleweibo.common.TLog;

public abstract class LauncherListAdapter<T> extends LauncherAdapter {

    private static final String TAG = "LancherListAdapter";
    private List<T> mData = null;
    private Launcher mLauncher = null;

    public LauncherListAdapter(List<T> data) {
        mData = data;
    }

    void setLauncher(Launcher launcher) {
        mLauncher = launcher;
    }

    public void delete(int position) {
        mLauncher.delete(position);
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
    public void moveFromTo(int from, int to) {
        TLog.debug(TAG, "onMoveFromTo from = %d, to = %d", from, to);
        
        T item = mData.remove(from);
        mData.add(to, item);
        
    }

    @Override
    public void remove(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
}
