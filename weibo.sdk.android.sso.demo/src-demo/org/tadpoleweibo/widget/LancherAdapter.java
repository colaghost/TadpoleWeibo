package org.tadpoleweibo.widget;

import android.widget.BaseAdapter;

public abstract class LancherAdapter extends BaseAdapter {
    public abstract void onSwapPosition(int from, int to);
    public abstract void onDelete(int position);
}
