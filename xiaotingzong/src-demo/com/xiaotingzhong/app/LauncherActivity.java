package com.xiaotingzhong.app;

import java.io.IOException;
import java.util.ArrayList;

import org.tadpole.R;
import org.tadpoleweibo.widget.AsyncRoundImageView;
import org.tadpoleweibo.widget.Launcher;
import org.tadpoleweibo.widget.LauncherListAdapter;
import org.tadpoleweibo.widget.SurfaceImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.response.Account;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.builder.UserBuilder;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;
import com.xiaotingzhong.app.storage.FriendsCacheMgr;
import com.xiaotingzhong.app.storage.SubscriptionMgr;

public class LauncherActivity extends Activity {
    static final String TAG = "LauncherActivity";

    static final int REQUEST_CODE_SUBSCRIPT = 1;

    private ImageButton mImgBtnAdd;
    private SurfaceImageView mImgViewBg;
    private Launcher mLauncher;
    private ArrayList<User> mUserList = new ArrayList();

    private LauncherListAdapter<User> mLauncherAdapter = null;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_launcher);
        this.mLauncher = ((Launcher) findViewById(R.id.launcher));
        this.mImgViewBg = ((SurfaceImageView) findViewById(R.id.surfaceimgview_bg));
        this.mImgBtnAdd = ((ImageButton) findViewById(R.id.imgbtn_add));
        this.mImgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), SubscriptionActivity.class);
                LauncherActivity.this.startActivityForResult(intent, REQUEST_CODE_SUBSCRIPT);
            }
        });

        final LauncherActivity me = this;
        mLauncherAdapter = new LauncherListAdapter<User>(mUserList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = LayoutInflater.from(me).inflate(R.layout.launche_page_item, null);
                TextView txtview = (TextView) v.findViewById(R.id.txtview_screen_name);
                AsyncRoundImageView asyncImageView = (AsyncRoundImageView) v.findViewById(R.id.asyncimgview_profile);
                View btnDel = v.findViewById(R.id.imgview_delete);
                User user = (User) mUserList.get(position);
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

        mLauncher.setDataAdapter(mLauncherAdapter);

        getUid();
    }

    public void fetchUserFriends() {
        SubscriptionMgr subscriptionMgr = new SubscriptionMgr(this, XTZApplication.app.curUid);
        ArrayList<User> usrLst = new FriendsCacheMgr(this, XTZApplication.app.curUid).getFriendsByUids(subscriptionMgr.getSubscriptedUids());
        fillLauncherData(usrLst);
    }

    public void getUid() {
        new AccountAPI(AccessTokenKeeper.readAccessToken(this)).getUid(new RequestListener() {
            @Override
            public void onIOException(IOException e) {
            }

            @Override
            public void onError(WeiboException e) {
            }

            @Override
            public void onComplete(String response) {
                XTZApplication.app.curUid = Account.fromGetUid(response).uid;
                fetchUserInfo(XTZApplication.app.curUid);
            }
        });
    }

    public void fetchUserInfo(final int uid) {
        Log.d(TAG, "fetchUserInfo " + mUserList.size());
        new UsersAPI(AccessTokenKeeper.readAccessToken(this)).show(uid, new RequestListener() {
            public void onComplete(String response) {
                User user = null;
                try {
                    user = UserBuilder.fromResponse(response);
                    XTZApplication.app.curUser = user;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                fetchUserFriends();
            }

            public void onError(WeiboException weiboE) {
            }

            public void onIOException(IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void fillLauncherData(final ArrayList<User> userList) {
        userList.add(0, XTZApplication.app.curUser);
        Log.d(TAG, "fillLauncherData " + userList.size());
        mUserList = userList;
        mLauncherAdapter.setList(mUserList);

        final LauncherActivity me = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLauncherAdapter.notifyDataSetChanged();
            }
        });

        //        runOnUiThread(new Runnable() {
        //            public void run() {
        //                LauncherListAdapter<User> adapter = new LauncherListAdapter<User>(userList) {
        //                    public View getView(int postion, View convertView, ViewGroup parent) {
        //                        View v = LayoutInflater.from(me).inflate(R.layout.launche_page_item, null);
        //                        TextView txtview = (TextView) v.findViewById(R.id.txtview_screen_name);
        //                        AsyncRoundImageView asyncImageView = (AsyncRoundImageView) v.findViewById(R.id.asyncimgview_profile);
        //                        View btnDel = v.findViewById(R.id.imgview_delete);
        //                        User user = (User) userList.get(postion);
        //                        txtview.setText(user.screen_name);
        //                        asyncImageView.setImageURL(user.profile_image_url);
        //
        //                        btnDel.setOnClickListener(new View.OnClickListener() {
        //                            public void onClick(View view) {
        //                                //                LauncherActivity.2.1.this.delete(this.val$position);
        //                            }
        //                        });
        //                        return v;
        //                    }
        //                };
        //                LauncherActivity.this.mLauncher.setDataAdapter(adapter);
        //            }
        //        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case REQUEST_CODE_SUBSCRIPT:
            fetchUserFriends();
            break;

        default:
            break;
        }

    }

}