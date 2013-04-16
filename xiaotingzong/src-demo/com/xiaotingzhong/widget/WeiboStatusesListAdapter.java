package com.xiaotingzhong.widget;

import org.tadpole.R;
import org.tadpoleweibo.app.LoadDialog;
import org.tadpoleweibo.common.StringUtil;
import org.tadpoleweibo.widget.AsyncRoundImageView;
import org.tadpoleweibo.widget.PageListViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.WeiboStatus;
import com.xiaotingzhong.app.StatusesActivity;

public class WeiboStatusesListAdapter extends PageListViewAdapter<WeiboStatus> {
    private static final String TAG = null;
    private User mUser;

    public WeiboStatusesListAdapter(Activity act, User user) {
        super(act);
        mUser = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeiboStatus weiboStatus = getItemData(position);
        View view = convertView; // reuse cache view
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_statuses, null);
            holder = new ViewHolder();
            holder.txtViewScreenName = (TextView) view.findViewById(R.id.txtview_screen_name);
            holder.txtViewText = (TextView) view.findViewById(R.id.txtview_text);
            holder.txtViewText.setClickable(true);
            holder.txtViewText.setMovementMethod(LinkMovementMethod.getInstance());

            holder.asycnImgViewProfile = (AsyncRoundImageView) view.findViewById(R.id.asyncimgview_profile);
            holder.asycnImgViewPic = (AsyncRoundImageView) view.findViewById(R.id.asyncimgview_pic);
            holder.asycnImgViewPic.setCornerRadius(0);


            holder.txtViewRetweetedText = (TextView) view.findViewById(R.id.txtview_retweeted_text);
            holder.txtViewRetweetedText.setClickable(true);
            holder.txtViewRetweetedText.setMovementMethod(LinkMovementMethod.getInstance());
            holder.asycnImgViewRetweetedPic = (AsyncRoundImageView) view.findViewById(R.id.asyncimgview_retweeted_pic);
            holder.asycnImgViewRetweetedPic.setCornerRadius(0);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txtViewScreenName.setText(mUser.screen_name);
        holder.txtViewText.setText(weiboStatus.text);
        holder.asycnImgViewProfile.setImageURL(mUser.profile_image_url);
        holder.txtViewText.setText(weiboStatus.getTextSpannaleString());

        if (!StringUtil.isEmpty(weiboStatus.thumbnail_pic)) {
            holder.asycnImgViewPic.setVisibility(View.VISIBLE);
            holder.asycnImgViewPic.setImageURL(weiboStatus.thumbnail_pic);
        } else {
            holder.asycnImgViewPic.setVisibility(View.GONE);
        }



        WeiboStatus retweeted = weiboStatus.retweeted_status;
        // weibo retweeted status
        if (weiboStatus.retweeted_status != null) {

            System.out.println(retweeted.user.screen_name);
            System.out.println(retweeted.text);

            holder.txtViewRetweetedText.setVisibility(View.VISIBLE);
            holder.asycnImgViewRetweetedPic.setVisibility(View.VISIBLE);
            holder.txtViewRetweetedText.setText(retweeted.getTextSpannaleString());

            if (StringUtil.isNotEmpty(retweeted.thumbnail_pic)) {
                holder.asycnImgViewRetweetedPic.setVisibility(View.VISIBLE);
                holder.asycnImgViewRetweetedPic.setImageURL(retweeted.thumbnail_pic);
            } else {
                holder.asycnImgViewRetweetedPic.setVisibility(View.GONE);
            }
        }
        // not
        else {
            holder.txtViewRetweetedText.setVisibility(View.GONE);
            holder.asycnImgViewRetweetedPic.setVisibility(View.GONE);
        }


        return view;
    }


    /**
     * j
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
        AsyncRoundImageView asycnImgViewPic;

        TextView txtViewRetweetedText;
        AsyncRoundImageView asycnImgViewRetweetedPic;
    }

    public static class ShowUserAsyncTask extends AsyncTask<String, String, User> {

        private String mScreenName;
        private long mUid;
        private Context mContext;
        private LoadDialog mLoadingDialog;

        public ShowUserAsyncTask(Context context, long uid, String screen_name) {
            mScreenName = screen_name;
            mUid = uid;
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog = new LoadDialog(mContext);
            mLoadingDialog.show();
        }

        @Override
        protected User doInBackground(String... params) {
            try {
                Thread.sleep(800);
                return User.getUserFromShowJson(mScreenName, mUid);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            mLoadingDialog.cancel();
            if (result == null) {
                Toast.makeText(mContext, "用户不存在", Toast.LENGTH_LONG).show();
            } else {
                StatusesActivity.start(mContext, result);
            }
        }
    }
}
