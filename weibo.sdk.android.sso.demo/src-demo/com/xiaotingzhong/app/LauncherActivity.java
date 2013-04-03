package com.xiaotingzhong.app;

import java.io.IOException;
import java.util.ArrayList;

import org.tadpole.R;
import org.tadpoleweibo.widget.AsyncImageView;
import org.tadpoleweibo.widget.Launcher;
import org.tadpoleweibo.widget.LauncherListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.ApiFactory;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.response.Account;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.builder.FriendShipsBuilder;
import com.weibo.sdk.android.api.response.builder.UserBuilder;
import com.weibo.sdk.android.demo.MainActivity;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 
 * @author liyan (liyan9@staff.sina.com.cn)
 */
public class LauncherActivity extends Activity {

    static final String TAG = "LauncherActivity";

    static final int FILL_LAUNCHER_DATA = 1;

    private Launcher mLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mLauncher = (Launcher) findViewById(R.id.launcher);
        fetchUid();
    }

    public void fillLauncherData(final ArrayList<User> userList) {
        final LauncherActivity me = this;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LauncherListAdapter<User> test = new LauncherListAdapter<User>(userList) {
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {

                        final View view = LayoutInflater.from(me).inflate(R.layout.launche_page_item, null);
                        TextView textView = (TextView) view.findViewById(R.id.txtview_name);
                        AsyncImageView imageView = (AsyncImageView) view.findViewById(R.id.imgview_image);
                        View deleteBtnView = view.findViewById(R.id.pageItemDeleteBtn);

                        User item = userList.get(position);

                        textView.setText(item.screen_name);
                        imageView.setImageURL(item.profile_image_url);


                        final View bg = view.findViewById(R.id.pageItemBg);
                        deleteBtnView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete(position);
                            }
                        });

                        //                if (BoardPageItem.COLOR_BLUE.equals(item.color)) {
                        bg.setBackgroundResource(R.drawable.blue);
                        //                } else {
                        //                    bg.setBackgroundResource(R.drawable.red);
                        //                }

                        //                if (Configure.isEditMode) {
                        bg.getBackground().setAlpha(220);
                        //                    if (item.editable) {
                        deleteBtnView.setVisibility(View.VISIBLE);
                        //                    } else {
                        //                deleteBtnView.setVisibility(View.INVISIBLE);
                        //                    }
                        //                } else {
                        //                    bg.getBackground().setAlpha(255);
                        //                    deleteBtnView.setVisibility(View.INVISIBLE);
                        //                }

                        //                if (item.hide) {
                        //                    view.setVisibility(View.INVISIBLE);
                        //                }
                        return view;
                    }
                };
                mLauncher.setDataAdapter(test);
            }
        });
    }


    public void fetchUid() {
        Log.d(TAG, "fetchUid");
        new AccountAPI(AccessTokenKeeper.readAccessToken(this)).getUid(new RequestListener() {

            @Override
            public void onIOException(IOException e) {
            }

            @Override
            public void onError(WeiboException e) {
            }

            @Override
            public void onComplete(String response) {
                Log.i(TAG, "response = " + response);
                XTZApplication.application.curLoginAccount = Account.fromResponse(response);
                LauncherActivity.this.fetchUserInfo(XTZApplication.application.curLoginAccount.uid);
            }
        });
    }

    private ArrayList<User> mUserList = new ArrayList<User>();

    public void fetchUserInfo(final int uid) {
        new UsersAPI(AccessTokenKeeper.readAccessToken(this)).show(uid, new RequestListener() {

            @Override
            public void onIOException(IOException e) {
            }

            @Override
            public void onError(WeiboException e) {
            }

            @Override
            public void onComplete(String response) {
                Log.i(TAG, "response = " + response);
                User usersShow = UserBuilder.fromResponse(response);
                mUserList.add(usersShow);

                LauncherActivity.this.fetchUserFriends(uid);
            }
        });
    }

    public void fetchUserFriends(int uid) {
        ApiFactory.getFriendShipsAPI(this).friends(uid, 50, 0, true, new RequestListener() {

            @Override
            public void onIOException(IOException e) {
            }

            @Override
            public void onError(WeiboException e) {
            }

            @Override
            public void onComplete(String response) {
                try {
                    ArrayList<User> list = FriendShipsBuilder.fromFriends(response);
                    mUserList.addAll(list);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                LauncherActivity.this.fillLauncherData(mUserList);
            }
        });
    }


}
