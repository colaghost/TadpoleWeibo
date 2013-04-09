package com.xiaotingzhong.app;

import java.io.IOException;
import java.util.ArrayList;

import org.tadpole.R;
import org.tadpoleweibo.widget.AsyncImageView;
import org.tadpoleweibo.widget.Launcher;
import org.tadpoleweibo.widget.LauncherListAdapter;
import org.tadpoleweibo.widget.SurfaceImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.builder.UserBuilder;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.app.storage.FriendsCacheMgr;
import com.xiaotingzhong.app.storage.SubscriptionMgr;

public class LauncherActivity extends Activity {
    static final String TAG = "LauncherActivity";
    private ImageButton mImgBtnAdd;
    private SurfaceImageView mImgViewBg;
    private Launcher mLauncher;
    private ArrayList<User> mUserList = new ArrayList();

    public void fetchUserFriends(int uid) {
        SubscriptionMgr subscriptionMgr = new SubscriptionMgr(this, uid);
        fillLauncherData(new FriendsCacheMgr(this, uid).getFriendsByUids(subscriptionMgr.getSubscriptedUids()));
    }

    public void fetchUserInfo(final int uid) {
        new UsersAPI(AccessTokenKeeper.readAccessToken(this)).show(uid, new RequestListener() {
            public void onComplete(String response) {
                User localUser = UserBuilder.fromResponse(response);
                mUserList.add(localUser);
                fetchUserFriends(uid);
            }

            public void onError(WeiboException weiboE) {
            }

            public void onIOException(IOException e) {
            }
        });
    }

    public void fillLauncherData(final ArrayList<User> userList) {
        final LauncherActivity me = this;
        runOnUiThread(new Runnable() {
            public void run() {
                LauncherListAdapter<User> adapter = new LauncherListAdapter<User>(userList) {
                    public View getView(int postion, View convertView, ViewGroup parent) {
                        View v = LayoutInflater.from(me).inflate(R.layout.launche_page_item, null);
                        TextView txtview = (TextView) v.findViewById(R.id.txtview_screen_name);
                        AsyncImageView asyncImageView = (AsyncImageView) v.findViewById(R.id.asyncimgview_profile);
                        View btnDel = v.findViewById(2131034139);
                        User user = (User) userList.get(postion);
                        txtview.setText(user.screen_name);
                        asyncImageView.setImageURL(user.profile_image_url);
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                //                LauncherActivity.2.1.this.delete(this.val$position);
                            }
                        });
                        return v;
                    }
                };
                LauncherActivity.this.mLauncher.setDataAdapter(adapter);
            }
        });
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_launcher);
        this.mLauncher = ((Launcher) findViewById(R.id.launcher));
        this.mImgViewBg = ((SurfaceImageView) findViewById(R.id.surfaceimgview_bg));
        this.mImgBtnAdd = ((ImageButton) findViewById(R.id.imgbtn_add));
        this.mImgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent intent = new Intent();
                //                localIntent.setClass(paramView.getContext(), SubscriptionActivity.class);
                //                LauncherActivity.this.startActivity(localIntent);
            }
        });
        fetchUserInfo(XTZApplication.curUid);
    }
}