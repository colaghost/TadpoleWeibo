
package com.xiaotingzhong.app;

import org.tadpole.R;
import org.tadpoleweibo.widget.settings.SettingsGroup;
import org.tadpoleweibo.widget.settings.SettingsItem;
import org.tadpoleweibo.widget.settings.SettingsItem.SettingsItemListener;
import org.tadpoleweibo.widget.settings.SettingsListView;
import org.tadpoleweibo.widget.settings.item.SettingsItemNav;
import org.tadpoleweibo.widget.settings.item.SettingsItemNormal;
import org.tadpoleweibo.widget.settings.item.SettingsItemOptions;
import org.tadpoleweibo.widget.settings.item.SettingsItemSwitcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends Activity {

    public static final String TAG = "SettingsActivity";

    private SettingsListView mSettingsView;

    /**
     * Use Explicit Intent start Activity
     * 
     * @param activity
     * @param uid
     */
    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSettingsView = (SettingsListView)this.findViewById(R.id.settingsview);

        mSettingsView.addGroup(createSettingsGroupApp());

        mSettingsView.addGroup(createSettingsGroupAboutMeInfo());

        mSettingsView.addGroup(createSettingsGroupOther());

    }

    /**
     * 应用设置
     */
    private SettingsGroup createSettingsGroupApp() {
        SettingsGroup group = new SettingsGroup();
        group.setTitle("应用设置");

        // 浏览器选择
        SettingsItemSwitcher itemBrowser = new SettingsItemSwitcher("使用内置浏览器",
                "内置浏览器如果不能使用请用外部浏览器", true);
        itemBrowser.setListener(new SettingsItemListener<Boolean>() {
            @Override
            public void onSettingsAction(SettingsItem<Boolean> item, Boolean params) {
                Log.d(TAG, "onSettingsAction = " + params);
            }
        });
        group.addItem(itemBrowser);

        // 图片模式

        String[] modeStrArr = {
                "只看图片", "只看文字"
        };

        SettingsItemOptions itemMode = new SettingsItemOptions("阅读模式", "亲！请根据流量切换模式哦", modeStrArr,
                0);
        itemMode.setListener(new SettingsItemListener<Integer>() {
            @Override
            public void onSettingsAction(SettingsItem item, Integer params) {
                Log.d(TAG, "onSettingsAction = " + params);
            }
        });
        group.addItem(itemMode);

        return group;
    }

    /**
     * 作者简介
     */
    private SettingsGroup createSettingsGroupAboutMeInfo() {
        SettingsGroup group = new SettingsGroup();
        group.setTitle("作者简介");

        // Browser
        SettingsItemNormal item = new SettingsItemNormal("英文名字", "Zenip");
        group.addItem(item);

        // Team
        item = new SettingsItemNormal("团队", "挨踢公寓");
        group.addItem(item);

        // Author QQ
        item = new SettingsItemNormal("QQ", "365916703");
        group.addItem(item);

        // Author Email
        item = new SettingsItemNormal("Email", "lxyczh@gmail.com");
        group.addItem(item);

        return group;
    }

    /**
     * 应用推荐
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    private SettingsGroup createSettingsGroupOther() {
        SettingsGroup g1 = new SettingsGroup();
        g1.setTitle("应用推荐");
        g1.addItem(new SettingsItemNav("为小听众打分", null));
        return g1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("hello ");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mSettingsView.notiyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
}
