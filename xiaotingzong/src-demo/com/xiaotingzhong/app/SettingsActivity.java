
package com.xiaotingzhong.app;

import org.tadpole.R;
import org.tadpoleweibo.widget.settings.SettingsItem;
import org.tadpoleweibo.widget.settings.SettingsGroup;
import org.tadpoleweibo.widget.settings.SettingsListView;
import org.tadpoleweibo.widget.settings.item.SettingsItemNav;
import org.tadpoleweibo.widget.settings.item.SettingsItemOptions;
import org.tadpoleweibo.widget.settings.item.SettingsItemSwitcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends Activity {

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

        SettingsGroup group = new SettingsGroup();
//        group.addItem(new SettingsItemOptions());
        group.addItem(new SettingsItemNav());
        group.addItem(new SettingsItemSwitcher());
        group.addItem(new SettingsItemSwitcher());

        mSettingsView.addGroup(group);

        SettingsGroup group1 = new SettingsGroup();
//        group1.addItem(new SettingsItemOptions());
        mSettingsView.addGroup(group1);

        SettingsGroup group2 = new SettingsGroup();
        group2.addItem(new SettingsItemSwitcher());
        mSettingsView.addGroup(group2);

        SettingsGroup group4 = new SettingsGroup();
        group4.addItem(new SettingsItemNav());
        group4.addItem(new SettingsItemNav());
        group4.addItem(new SettingsItemSwitcher());
//        group4.addItem(new SettingsItemOptions());

        mSettingsView.addGroup(group4);
    }

    private SettingsListView mSettingsView;

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
