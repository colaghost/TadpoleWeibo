package com.xiaotingzhong.widget;

import org.tadpole.R;
import org.tadpoleweibo.widget.PageListViewAdapter;

import com.weibo.sdk.android.api.response.WeiboStatuses;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeiboStatusesListAdapter extends PageListViewAdapter<WeiboStatuses> {

    public WeiboStatusesListAdapter(Activity act) {
        super(act);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getLayoutInflater().inflate(R.layout.listitem_statuses, null);
        return view;
    }
}
