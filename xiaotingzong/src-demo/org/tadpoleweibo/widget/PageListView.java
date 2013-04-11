package org.tadpoleweibo.widget;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PageListView<T> extends AbsPageListView<T> {

    public PageListView(Context context) {
        super(context);
    }

    public PageListView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

}