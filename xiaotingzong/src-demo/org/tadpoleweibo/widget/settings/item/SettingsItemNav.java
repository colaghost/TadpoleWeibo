
package org.tadpoleweibo.widget.settings.item;

import org.tadpole.R;
import org.tadpoleweibo.widget.settings.SettingsItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SettingsItemNav extends SettingsItem<String> {

    public SettingsItemNav(String title, String summary) {
        super(title, summary);
    }

    @Override
    public View createRightDetailView(LayoutInflater inflater, ViewGroup parent) {
        ImageView imageView = (ImageView)inflater.inflate(R.layout.tp_settings_item_right_nav,
                parent, false);
        return imageView;
    }

}
