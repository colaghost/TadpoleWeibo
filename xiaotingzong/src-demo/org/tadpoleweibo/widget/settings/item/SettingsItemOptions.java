
package org.tadpoleweibo.widget.settings.item;

import java.util.ArrayList;

import org.tadpole.R;
import org.tadpoleweibo.widget.settings.SettingsContextMenu;
import org.tadpoleweibo.widget.settings.SettingsContextMenu.ISettingMenuListener;
import org.tadpoleweibo.widget.settings.SettingsItem;
import org.tadpoleweibo.widget.settings.SettingsItemInfo;

import android.media.audiofx.BassBoost.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsItemOptions extends SettingsItem<Integer> implements ISettingMenuListener {

    static final int INVALID_SELECT_INDEX = -1;

    private String[] mOptions = null;

    private int mSelectedIndex = INVALID_SELECT_INDEX;

    public SettingsItemOptions(String title, String summary, String[] options) {
        super(title, summary);
        mOptions = options;
    }

    public SettingsItemOptions(String title, String summary, String[] options, int selectedIndex) {
        super(title, summary);
        mOptions = options;
        mSelectedIndex = selectedIndex;
    }

    @Override
    public View createRightDetailView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.tp_settings_item_right_nav, parent, false);
    }

    @Override
    public void onItemClick(ViewGroup itemRoot) {
        super.onItemClick(itemRoot);
        SettingsContextMenu menu = new SettingsContextMenu(itemRoot.getContext());
        menu.setOptions(mOptions, mSelectedIndex, this);
    }

    @Override
    public void onSettingContextMenuClick(int index) {
        notifyListener(Integer.valueOf(index));
    }

    @Override
    public void onSettingContextMenuDismiss() {

    }
    
    
    public static void main(String[] args) {
        SettingsItemOptions sio = new SettingsItemOptions("", "", new String[]{});
    }
}
