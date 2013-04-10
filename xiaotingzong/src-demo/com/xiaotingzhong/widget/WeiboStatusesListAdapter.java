package com.xiaotingzhong.widget;

import org.tadpoleweibo.widget.PageListViewAdapter;

import com.weibo.sdk.android.api.response.WeiboStatuses;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class WeiboStatusesListAdapter extends PageListViewAdapter<WeiboStatuses> {

    public WeiboStatusesListAdapter(Activity act) {
        super(act);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
