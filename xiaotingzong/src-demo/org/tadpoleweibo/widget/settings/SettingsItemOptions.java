
package org.tadpoleweibo.widget.settings;

import org.tadpole.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsItemOptions extends SettingsItem {

    @Override
    public View createRightDetailView(LayoutInflater inflater, ViewGroup parent) {
        inflater.inflate(R.layout.tp_settings_item_right_nav, parent);
        return null;
    }

}
