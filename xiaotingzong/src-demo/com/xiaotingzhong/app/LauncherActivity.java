package com.xiaotingzhong.app;

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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.weibo.sdk.android.api.response.User;
import com.xiaotingzhong.app.storage.FriendsCacheMgr;
import com.xiaotingzhong.app.storage.SubscriptionMgr;

public class LauncherActivity extends Activity implements AdapterView.OnItemClickListener {
    static final String TAG = "LauncherActivity";
    static final int REQUEST_CODE_SUBSCRIPT = 1;

    static final String UID = "uid";
    static final String USER = "user";

    /**
     * Use Explicit Intent start Activity
     * 
     * @param activity
     * @param uid
     */
    public static void start(Activity activity, int uid, User user) {
        Intent intent = new Intent();
        intent.putExtra(UID, uid);
        intent.putExtra(USER, user);
        intent.setClass(activity, LauncherActivity.class);
        activity.startActivity(intent);
    }

    private ImageButton mImgBtnAdd;
    private SurfaceImageView mImgViewBg;
    private Launcher mLauncher;
    private ArrayList<User> mUserList = new ArrayList();
    private LauncherListAdapter<User> mLauncherAdapter = null;
    private int mUidSelf = 0;
    private User mUserSelf = null;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        final LauncherActivity me = this;

        // populate extra
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        mUidSelf = extra.getInt(UID);
        mUserSelf = (User) extra.getSerializable(USER);

        // init views
        setContentView(R.layout.activity_launcher);
        this.mLauncher = ((Launcher) findViewById(R.id.launcher));
        this.mImgViewBg = ((SurfaceImageView) findViewById(R.id.surfaceimgview_bg));
        this.mImgBtnAdd = ((ImageButton) findViewById(R.id.imgbtn_add));
        this.mImgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SubscriptionActivity.startForResult(me, mUserSelf, REQUEST_CODE_SUBSCRIPT);
            }
        });

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
                    }
                });
                return v;
            }
        };
        mLauncher.setDataAdapter(mLauncherAdapter);
        mLauncher.setOnItemClickListener(this);
        // fetchUserInfo
        fetchUserFriends();
    }

    public void fetchUserFriends() {
        SubscriptionMgr subscriptionMgr = new SubscriptionMgr(this, mUidSelf);
        ArrayList<User> userList = new FriendsCacheMgr(this, mUidSelf).getFriendsByUids(subscriptionMgr.getSubscriptedUids());
        fillLauncherData(userList);
    }

    public void fillLauncherData(final ArrayList<User> userList) {
        userList.add(0, mUserSelf);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = mUserList.get(position);
        Log.d(TAG, "onItemClick  position =  " + position + " userId =  " + user.id);
        StatusesActivity.start(this, user);
    }

}