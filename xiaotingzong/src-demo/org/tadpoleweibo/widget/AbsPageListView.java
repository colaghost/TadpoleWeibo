package org.tadpoleweibo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public abstract class AbsPageListView<T> extends PullToRefreshListView implements OnScrollListener, OnRefreshListener2<ListView> {
    private static final String TAG = "PageListView";
    private PageListViewAdapter<T> mAdapter;
    private OnLoadPageListListener<T> mLoadPageListener = null;
    private int mNextStartIndex;
    private PageList<T> mPageLst = null;
    private int mTotalCount;
    public int maxResult = 20;

    public AbsPageListView(Context paramContext) {
        super(paramContext);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    }

    public AbsPageListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public void doLoad() {
        this.mLoadPageListener.onLoadNext(this.mNextStartIndex, this.maxResult);
    }

    public PageList<T> getPageList() {
        return this.mPageLst;
    }

    public void setAdapter(ListAdapter adapter) {
        this.mAdapter = ((PageListViewAdapter) adapter);
        super.setAdapter(adapter);
        setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mLoadPageListener.onRefreshToGetNew(maxResult);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
    }

    public void setOnLoadPageListListener(OnLoadPageListListener<T> listener) {
        this.mLoadPageListener = listener;
    }

    public void setPageList(PageList<T> pageList) {
        PageListViewAdapter finalAdapter = this.mAdapter;
        if (finalAdapter != null && pageList != null) {
            finalAdapter.setList(pageList.records);
            this.mTotalCount = pageList.totalCount;
            this.mNextStartIndex += pageList.records.size();
            finalAdapter.notifyDataSetChanged();
        }
    }

    public static abstract interface OnLoadPageListListener<T> {
        public abstract PageList<T> onLoadNext(int paramInt1, int paramInt2);

        public abstract PageList<T> onRefreshToGetNew(int paramInt);
    }
}