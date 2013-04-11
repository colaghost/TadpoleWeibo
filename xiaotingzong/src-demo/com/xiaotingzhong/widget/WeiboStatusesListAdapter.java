package com.xiaotingzhong.widget;

import org.tadpole.R;
import org.tadpoleweibo.widget.AsyncRoundImageView;
import org.tadpoleweibo.widget.PageListViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.WeiboStatuses;

public class WeiboStatusesListAdapter extends PageListViewAdapter<WeiboStatuses> {
    private User mUser;
    
    public WeiboStatusesListAdapter(Activity act, User user) {
        super(act);
        mUser = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeiboStatuses weiboStatus = getItemData(position);
        View view = convertView; // reuse cache view
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_statuses, null);
            holder = new ViewHolder();
            holder.txtViewScreenName = (TextView) view.findViewById(R.id.txtview_screen_name);
            holder.txtViewText = (TextView) view.findViewById(R.id.txtview_text);
            holder.asycnImgViewProfile = (AsyncRoundImageView) view.findViewById(R.id.asyncimgview_profile);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txtViewScreenName.setText(mUser.screen_name);
        holder.txtViewText.setText(weiboStatus.text);
        holder.asycnImgViewProfile.setImageURL(mUser.profile_image_url);
        return view;
    }


    /**
     * 
     * use holder to avoid calling findViewById . Make Code Speed Up
     * 
     * <br>==========================
     * <br> author：Zenip
     * <br> email：lxyczh@gmail.com
     * <br> create：2013-4-9
     * <br>==========================
     */
    static class ViewHolder {
        TextView txtViewScreenName;
        TextView txtViewText;
        AsyncRoundImageView asycnImgViewProfile;
    }
}
