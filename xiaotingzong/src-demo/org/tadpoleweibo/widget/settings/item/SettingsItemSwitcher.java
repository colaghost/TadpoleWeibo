
package org.tadpoleweibo.widget.settings.item;

import org.tadpole.R;
import org.tadpoleweibo.widget.SwitchButton;
import org.tadpoleweibo.widget.settings.SettingsItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsItemSwitcher extends SettingsItem<Boolean> {

    private SwitchButton mSwitcher;

    public SettingsItemSwitcher(String title, String summary) {
        super(title, summary);
    }

    @Override
    public View createRightDetailView(LayoutInflater inflater, ViewGroup parent) {
        mSwitcher = (SwitchButton)inflater.inflate(R.layout.tp_settings_item_right_switcher, parent, false);
        return mSwitcher;
    }

    @Override
    public void onItemClick(ViewGroup itemRoot) {
        super.onItemClick(itemRoot);
        mSwitcher.toggle();
    }
}
