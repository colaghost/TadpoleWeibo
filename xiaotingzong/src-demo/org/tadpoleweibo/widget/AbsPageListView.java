
package org.tadpoleweibo.widget;

import org.tadpole.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public abstract class AbsPageListView<T> extends PullToRefreshListView {
    private static final String TAG = "PageListView";

    static final int DIVIDER_HEIGHT = 1;

    static final int FADING_LENGTH = 0;

    public AbsPageListView(Context paramContext) {
        super(paramContext);
        init();
    }

    public AbsPageListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void firePullDownToRefresh() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (getHeaderSize() == 0) {
                    firePullDownToRefresh();
                } else {
                    setRefreshing();
                }

            }
        }, getContext().getResources().getInteger(R.integer.tp_config_animationDuration));
    }

    public void proxyOnRefreshComplete() {
        this.postDelayed(new Runnable() {

            @Override
            public void run() {
                onRefreshComplete();
            }
        }, 200);
    }

    private void init() {
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        ListView listView = (ListView)this.getRefreshableView();
        listView.setDividerHeight(DIVIDER_HEIGHT);
        listView.setDivider(getResources().getDrawable(R.drawable.divider));
        listView.setVerticalScrollBarEnabled(false);
        listView.setFadingEdgeLength(FADING_LENGTH);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

}
