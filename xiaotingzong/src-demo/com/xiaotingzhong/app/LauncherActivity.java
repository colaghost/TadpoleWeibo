
package com.xiaotingzhong.app;

import com.xiaotingzhong.broadcast.SubscriptReceiver;
import com.xiaotingzhong.model.DaoFactory;
import com.xiaotingzhong.model.User;
import com.xiaotingzhong.model.cache.userprivate.SubscriptionCache;

import org.tadpole.R;
import org.tadpoleweibo.widget.AsyncRoundImageView;
import org.tadpoleweibo.widget.Launcher;
import org.tadpoleweibo.widget.Launcher.OnDataChangeListener;
import org.tadpoleweibo.widget.LauncherListAdapter;
import org.tadpoleweibo.widget.SurfaceImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPagerEX.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class LauncherActivity extends Activity implements AdapterView.OnItemClickListener,
        OnPageChangeListener {
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
    public static void start(Activity activity, User user) {
        Intent intent = new Intent();
        intent.putExtra(USER, user);
        intent.setClass(activity, LauncherActivity.class);
        activity.startActivity(intent);
    }

    private ImageButton mImgBtnAdd;

    private ImageButton mImgBtnSet;

    private TextView mTxtViewPageTip;

    private SurfaceImageView mImgViewBg;

    private Launcher mLauncher;

    private AsyncRoundImageView mAsyncImgView;

    private ArrayList<User> mUserList = new ArrayList();

    private LauncherListAdapter<User> mLauncherAdapter = null;

    private User mUserSelf = null;

    private SubscriptReceiver mSubscriptReceiver;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "===== onCreate =====");
        final LauncherActivity me = this;

        // populate extra
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        mUserSelf = (User)extra.getSerializable(USER);

        // init views
        setContentView(R.layout.activity_launcher);
        this.mLauncher = ((Launcher)findViewById(R.id.launcher));
        this.mImgViewBg = ((SurfaceImageView)findViewById(R.id.surfaceimgview_bg));

        mTxtViewPageTip = (TextView)findViewById(R.id.txtview_page_tip);

        this.mImgBtnAdd = ((ImageButton)findViewById(R.id.imgbtn_add));
        this.mImgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SubscriptionActivity.startForResult(me, mUserSelf, REQUEST_CODE_SUBSCRIPT);
            }
        });

        mImgBtnSet = (ImageButton)findViewById(R.id.imgbtn_set);
        mImgBtnSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity act = (Activity)v.getContext();
                SettingsActivity.start(act);
            }
        });

        mLauncherAdapter = new LauncherListAdapter<User>(mUserList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = LayoutInflater.from(me).inflate(R.layout.launche_page_item, null);
                TextView txtview = (TextView)v.findViewById(R.id.txtview_screen_name);
                AsyncRoundImageView asyncImageView = (AsyncRoundImageView)v
                        .findViewById(R.id.asyncimgview_profile);
                View btnDel = v.findViewById(R.id.imgview_delete);
                User user = (User)mUserList.get(position);
                txtview.setText(user.screen_name);
                asyncImageView.setImageURL(user.profile_image_url);

                btnDel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                    }
                });
                return v;
            }
        };

        System.out.println("mLauncherAdapter = " + mLauncherAdapter.getCount());

        mLauncher.setDataAdapter(mLauncherAdapter);
        mLauncher.setOnItemClickListener(this);
        mLauncher.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onChange() {
                updatePageTip();

                ArrayList<Long> uidList = new ArrayList<Long>();

                // save launcher item's order
                for (User user : mUserList) {
                    uidList.add(user.id);
                }

                // resave launcher items to change order
                DaoFactory.getSubscriptionDao(mUserSelf.id).saveSubscript(uidList);
            }
        });

        mLauncher.setOnPageChangeListener(this);

        mAsyncImgView = (AsyncRoundImageView)findViewById(R.id.asyncimgview_my);
        mAsyncImgView.setImageURL(mUserSelf.profile_image_url);

        // register subscription change
        mSubscriptReceiver = new SubscriptReceiver(this);
        mSubscriptReceiver.register();

        // fetchUserInfo
        fetchUserFriends();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updatePageTip();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void updatePageTip() {
        if (null == mLauncher) {
            return;
        }

        StringBuilder sb = new StringBuilder("");
        sb.append(mLauncher.getCurrentItem() + 1);
        sb.append("/");
        sb.append(mLauncher.getPageCount());

        mTxtViewPageTip.setText(sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptReceiver.unRegister();
        Log.d(TAG, "===== onDestroy =====");
    }

    public void fetchUserFriends() {
        SubscriptionCache subscriptionMgr = new SubscriptionCache(this, mUserSelf.id);
        ArrayList<User> userList = DaoFactory.getFriendsDao(mUserSelf.id).getUsersByUids(
                subscriptionMgr.getSubscriptedUids());
        fillLauncherData(userList);
    }

    public void fillLauncherData(final ArrayList<User> userList) {
        Log.d(TAG, "fillLauncherData " + userList.size());
        mUserList = userList;
        mLauncherAdapter.setList(mUserList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLauncherAdapter.notifyDataSetChanged();
                updatePageTip();
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
        StatusesActivity.start(this, user, mUserSelf.getRelateUserState(user));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, " mImgViewBg.setZOrderOnTop(true);");
        menu.add(0, 2, 2, " mImgViewBg.setZOrderOnTop(false);");
        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                mImgViewBg.setZOrderOnTop(true);
                break;
            case 2:
                mImgViewBg.setZOrderOnTop(false);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
