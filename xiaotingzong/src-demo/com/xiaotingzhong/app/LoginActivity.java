
package com.xiaotingzhong.app;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboDialog;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.util.Utility;
import com.xiaotingzhong.broadcast.SubscriptReceiver;
import com.xiaotingzhong.model.Account;
import com.xiaotingzhong.model.DaoFactory;
import com.xiaotingzhong.model.Emotion;
import com.xiaotingzhong.model.User;
import com.xiaotingzhong.model.cache.userprivate.SubscriptionCache;
import com.xiaotingzhong.widget.WeiboLoginListener;
import com.xiaotingzhong.widget.WeiboLoginWebView;

import org.tadpole.R;
import org.tadpoleweibo.app.LoadDialogAsyncTask;
import org.tadpoleweibo.app.NavBarActivity;
import org.tadpoleweibo.view.TadpoleWebView;
import org.tadpoleweibo.widget.AsyncRoundImageView;
import org.tadpoleweibo.widget.Launcher;
import org.tadpoleweibo.widget.Launcher.OnDataChangeListener;
import org.tadpoleweibo.widget.LauncherListAdapter;
import org.tadpoleweibo.widget.SurfaceImageView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.view.ViewPagerEX.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends NavBarActivity implements WeiboLoginListener {
    static final String TAG = "LoginActivity";


    static final int REQUEST_CODE_SUBSCRIPT = 1;

    static final String UID = "uid";

    static final String USER = "user";

    private WeiboLoginWebView mWebView;

    /**
     * Use Explicit Intent start Activity
     * 
     * @param activity
     * @param uid
     */
    public static void start(Activity activity, User user) {
        Intent intent = new Intent();
        intent.putExtra(USER, user);
        intent.setClass(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getNavBar().setTitle("登录");
        getNavBar().getBtnRight().setVisibility(View.INVISIBLE);
        
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
        if(token.isSessionValid()){
            startLauncherActivity();
            return;
        }
        

        mWebView = (WeiboLoginWebView)findViewById(R.id.webview);
        mWebView.setLoginListener(this);
        mWebView.login();
    }

    @Override
    public void onComplete(Oauth2AccessToken token) {
        AccessTokenKeeper.keepAccessToken(this, token);
        startLauncherActivity();
    }

    @Override
    public void onWeiboException(WeiboException e) {
        
    }

    @Override
    public void onWebViewError(int errorCode, String failingUrl, String description) {
        
    }

    @Override
    public void onCancel() {
        
    }
    
    
    public void startLauncherActivity() {
        final Activity me = this;
        new LoadDialogAsyncTask<String, String, Boolean>(me) {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    // 获取用户信息
                    XTZApplication.setCurUser(Account.getUserPreferCache());
                   
                    
                    // 获取表情 2013.6.10, 读文件IO太慢，不应该阻塞主流程，故心开辟县城
                    Runnable runnable = new Runnable() {
                        
                        @Override
                        public void run() {
                            try {
                                Emotion.cacheEmotions();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.setPriority(Thread.MIN_PRIORITY); 
                    t.start();
                    // 获取表情end
                    
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            };

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result.booleanValue() == true) {
                    LauncherActivity.start(me, XTZApplication.getCurUser());
                    finish();
                } else {
                    Toast.makeText(me, "获取用户信息失败", Toast.LENGTH_LONG).show();
                }
            }
        }.execute("");
    }
}
