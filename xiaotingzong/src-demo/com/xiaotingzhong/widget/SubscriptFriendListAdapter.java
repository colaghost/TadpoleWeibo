package com.xiaotingzhong.widget;

import org.tadpole.R;
import org.tadpoleweibo.widget.AsyncRoundImageView;
import org.tadpoleweibo.widget.PageListViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.weibo.sdk.android.api.response.User;
import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.app.storage.SubscriptionMgr;

public class SubscriptFriendListAdapter extends PageListViewAdapter<User> {

    private SubscriptionMgr subscriptMgr = null;;

    public SubscriptFriendListAdapter(Activity act) {
        super(act);
        subscriptMgr = new SubscriptionMgr(act, XTZApplication.app.curUid);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = (User) getItemData(position);
        View view = convertView; // reuse cache view
        ViewHolder holder = null;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_subscription, null);
            holder = new ViewHolder();
            holder.asyncImgViewProfile = (AsyncRoundImageView) view.findViewById(R.id.asyncimgview_profile);
            holder.txtViewScreenName = (TextView) view.findViewById(R.id.txtview_screen_name);
            holder.subscriptBtn = (ImageButton) view.findViewById(R.id.imgbtn_add);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.asyncImgViewProfile.setImageURL(user.profile_image_url);
        holder.txtViewScreenName.setText(user.screen_name);

        final ImageButton finalSubsctriptBtn = holder.subscriptBtn;
        if (subscriptMgr.isSubscripted(user.id)) {
            finalSubsctriptBtn.setImageResource(R.drawable.rootblock_add_cell_setok);
        } else {
            finalSubsctriptBtn.setImageResource(R.drawable.icon_addmsg);
        }

        holder.subscriptBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                if (subscriptMgr.isSubscripted(user.id)) {
                    subscriptMgr.unSubscript(user.id);
                    finalSubsctriptBtn.setImageResource(R.drawable.icon_addmsg);
                } else {
                    subscriptMgr.subscript(user.id);
                    finalSubsctriptBtn.setImageResource(R.drawable.rootblock_add_cell_setok);
                }
            }
        });
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
        ImageButton subscriptBtn;
        TextView txtViewScreenName;
        AsyncRoundImageView asyncImgViewProfile;
    }

}
