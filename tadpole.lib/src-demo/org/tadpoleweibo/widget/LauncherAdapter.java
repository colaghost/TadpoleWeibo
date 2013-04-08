package org.tadpoleweibo.widget;

import android.widget.BaseAdapter;

public abstract class LauncherAdapter extends BaseAdapter {
    public abstract void moveFromTo(int from, int to);
    public abstract void remove(int position);
}
