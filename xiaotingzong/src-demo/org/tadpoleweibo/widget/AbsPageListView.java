package org.tadpoleweibo.widget;

import org.tadpole.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public abstract class AbsPageListView<T> extends PullToRefreshListView {
    private static final String TAG = "PageListView";

    public AbsPageListView(Context paramContext) {
        super(paramContext);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    }

    public AbsPageListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        ListView listView = (ListView) this.getRefreshableView();
        listView.setDividerHeight(1);
        listView.setDivider(getResources().getDrawable(R.drawable.divider));
        listView.setVerticalScrollBarEnabled(false);
    }
}