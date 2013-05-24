
package com.xiaotingzhong.widget;

import com.xiaotingzhong.model.User;
import com.xiaotingzhong.model.cache.userprivate.SubscriptionCache;
import com.xiaotingzhong.model.dao.ISubscriptionDao;

import org.tadpole.R;
import org.tadpoleweibo.widget.AsyncRoundImageView;
import org.tadpoleweibo.widget.PageListViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class SubscriptFriendListAdapter extends PageListViewAdapter<User> {

    private ISubscriptionDao mSubscriptMgr = null;;

    public SubscriptFriendListAdapter(Activity act, User user) {
        super(act);
        mSubscriptMgr = new SubscriptionCache(act, user.id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = (User)getItemData(position);
        View view = convertView; // reuse cache view
        ViewHolder holder = null;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_subscription, null);
            holder = new ViewHolder();
            holder.asyncImgViewProfile = (AsyncRoundImageView)view
                    .findViewById(R.id.asyncimgview_profile);
            holder.txtViewScreenName = (TextView)view.findViewById(R.id.txtview_screen_name);
            holder.subscriptBtn = (ImageButton)view.findViewById(R.id.imgbtn_add);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        holder.asyncImgViewProfile.setImageURL(user.profile_image_url);
        holder.txtViewScreenName.setText(user.screen_name);

        final ImageButton finalSubsctriptBtn = holder.subscriptBtn;
        if (mSubscriptMgr.isSubscripted(user.id)) {
            finalSubsctriptBtn.setImageResource(R.drawable.rootblock_add_cell_setok);
        } else {
            finalSubsctriptBtn.setImageResource(R.drawable.icon_addmsg);
        }

        holder.subscriptBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                if (mSubscriptMgr.isSubscripted(user.id)) {
                    mSubscriptMgr.unSubscript(user.id);
                    finalSubsctriptBtn.setImageResource(R.drawable.icon_addmsg);
                } else {
                    mSubscriptMgr.subscript(user.id);
                    finalSubsctriptBtn.setImageResource(R.drawable.rootblock_add_cell_setok);
                }
            }
        });
        return view;
    }

    /**
     * use holder to avoid calling findViewById . Make Code Speed Up <br>=
     * ========================= <br>
     * author：Zenip <br>
     * email：lxyczh@gmail.com <br>
     * create：2013-4-9 <br>=
     * =========================
     */
    static class ViewHolder {
        ImageButton subscriptBtn;

        TextView txtViewScreenName;

        AsyncRoundImageView asyncImgViewProfile;
    }

}
