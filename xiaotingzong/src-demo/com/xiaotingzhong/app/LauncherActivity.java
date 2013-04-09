package com.xiaotingzhong.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.tadpoleweibo.widget.AsyncImageView;
import org.tadpoleweibo.widget.Launcher;
import org.tadpoleweibo.widget.LauncherListAdapter;
import org.tadpoleweibo.widget.SurfaceImageView;

public class LauncherActivity extends Activity {
    static final String TAG = "LauncherActivity";
    private ImageButton mImgBtnAdd;
    private SurfaceImageView mImgViewBg;
    private Launcher mLauncher;
    private ArrayList<User> mUserList = new ArrayList();

    public void fetchUserFriends(int uid) {
        SubscriptionMgr localSubscriptionMgr = new SubscriptionMgr(this, uid);
        fillLauncherData(new FriendsCacheMgr(this, uid).getFriendsByUids(localSubscriptionMgr.getSubscriptedUids()));
    }

    public void fetchUserInfo(final int uid) {
        new UsersAPI(AccessTokenKeeper.readAccessToken(this)).show(uid, new RequestListener() {
            public void onComplete(String paramString) {
                Log.i("LauncherActivity", "response = " + paramString);
                User localUser = UserBuilder.fromResponse(paramString);
                LauncherActivity.this.mUserList.add(localUser);
                LauncherActivity.this.fetchUserFriends(uid);
            }

            public void onError(WeiboException paramWeiboException) {
            }

            public void onIOException(IOException paramIOException) {
            }
        });
    }

    public void fillLauncherData(final ArrayList<User> paramArrayList) {
        final LauncherActivity me = this;
        runOnUiThread(new Runnable() {
            public void run() {
                LauncherListAdapter<User> adapter = new LauncherListAdapter(paramArrayList) {
                    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
                        View localView1 = LayoutInflater.from(me).inflate(2130903043, null);
                        TextView localTextView = (TextView) localView1.findViewById(2131034138);
                        AsyncImageView localAsyncImageView = (AsyncImageView) localView1.findViewById(2131034137);
                        View localView2 = localView1.findViewById(2131034139);
                        User localUser = (User) paramArrayList.get(paramInt);
                        localTextView.setText(localUser.screen_name);
                        localAsyncImageView.setImageURL(localUser.profile_image_url);
                        localView1.findViewById(2131034136);
                        localView2.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View paramView) {
                                //                LauncherActivity.2.1.this.delete(this.val$position);
                            }
                        });
                        return localView1;
                    }
                };
                LauncherActivity.this.mLauncher.setDataAdapter(adapter);
            }
        });
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getWindow().setFlags(16777216, 16777216);
        setContentView(2130903040);
        this.mLauncher = ((Launcher) findViewById(2131034125));
        this.mImgViewBg = ((SurfaceImageView) findViewById(2131034124));
        this.mImgBtnAdd = ((ImageButton) findViewById(2131034127));
        this.mImgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent localIntent = new Intent();
                localIntent.setClass(paramView.getContext(), SubscriptionActivity.class);
                LauncherActivity.this.startActivity(localIntent);
            }
        });
        fetchUserInfo(XTZApplication.curUid);
    }
}