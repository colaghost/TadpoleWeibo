
package org.tadpoleweibo.widget.settings.item;

import org.tadpole.R;
import org.tadpoleweibo.widget.settings.SettingsItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SettingsItemNormal extends SettingsItem<String> {

    public SettingsItemNormal(String title, String summary) {
        super(title, summary);
    }

    @Override
    public View createRightDetailView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

}
