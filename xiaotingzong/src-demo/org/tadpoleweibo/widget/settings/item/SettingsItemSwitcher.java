
package org.tadpoleweibo.widget.settings.item;

import org.tadpole.R;
import org.tadpoleweibo.widget.SwitchButton;
import org.tadpoleweibo.widget.SwitchButton.SwitchListener;
import org.tadpoleweibo.widget.settings.SettingsItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsItemSwitcher extends SettingsItem<Boolean> implements SwitchListener {
    
    public static final String TAG = "SettingsItemSwitcher";

    private SwitchButton mSwitcher;

    private boolean mIsTurnOn = false;

    public SettingsItemSwitcher(String title, String summary, boolean defaultValue) {
        super(title, summary);
        mIsTurnOn = defaultValue;
    }

    @Override
    public View createRightDetailView(LayoutInflater inflater, ViewGroup parent) {
        mSwitcher = (SwitchButton)inflater.inflate(R.layout.tp_settings_item_right_switcher,
                parent, false);
        mSwitcher.setSwitchListener(this);
        mSwitcher.setTurnOn(mIsTurnOn);
        return mSwitcher;
    }

    @Override
    public void onItemClick(ViewGroup itemRoot) {
        mSwitcher.toggle();
    }

    @Override
    public void on() {
        notifyListener(true);
    }

    @Override
    public void off() {
        notifyListener(false);
    }
}
