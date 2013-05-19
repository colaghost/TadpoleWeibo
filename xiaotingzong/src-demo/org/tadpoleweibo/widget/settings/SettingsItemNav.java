
package org.tadpoleweibo.widget.settings;

import org.tadpole.R;
import org.tadpoleweibo.widget.SwitchButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsItemNav extends SettingsItem {

    @Override
    public View createRightDetailView(LayoutInflater inflater, ViewGroup parent) {
        inflater.inflate(R.layout.tp_settings_item_right_nav, parent);
        return null;
    }

}
