
package org.tadpoleweibo.widget.settings.item;

import org.tadpole.R;
import org.tadpoleweibo.widget.settings.SettingsContextMenu;
import org.tadpoleweibo.widget.settings.SettingsContextMenu.ISettingMenuListener;
import org.tadpoleweibo.widget.settings.SettingsItem;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsItemOptions extends SettingsItem<Integer> implements ISettingMenuListener {

    static final int INVALID_SELECT_INDEX = -1;

    private String[] mOptions = null;

    private int mSelectedIndex = INVALID_SELECT_INDEX;

    private TextView mTextViewOptions = null;

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
        mTextViewOptions = (TextView)inflater.inflate(R.layout.tp_settings_item_right_options,
                parent, false);

        updateText();

        return mTextViewOptions;
    }

    @Override
    public void onItemClick(ViewGroup itemRoot) {
//        setBackgroundSelected(true);

        Resources res = itemRoot.getContext().getResources();

        int[] location = {
                0, 0
        };

        itemRoot.getLocationInWindow(location);

        int x = (int)(location[0] + itemRoot.getWidth() - res
                .getDimension(R.dimen.tp_settings_menu_right));
        int y = (int)(location[1] - res.getDimension(R.dimen.tp_settings_menu_top));

        SettingsContextMenu menu = new SettingsContextMenu(itemRoot.getContext());
        menu.setPos(x, y);
        menu.setOptions(mOptions, mSelectedIndex, this);
        menu.show();
    }

    @Override
    public void onSettingContextMenuClick(int index) {
        mSelectedIndex = index;
        updateText();
        notifyListener(Integer.valueOf(index));
    }

    @Override
    public void onSettingContextMenuDismiss() {
        setBackgroundSelected(false);
    }

    private void updateText() {
        if (INVALID_SELECT_INDEX != mSelectedIndex) {
            mTextViewOptions.setText(mOptions[mSelectedIndex]);
        }
    }
}
