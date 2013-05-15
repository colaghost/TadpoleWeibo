
package org.tadpoleweibo.widget.settings;

import org.tadpole.R;
import org.tadpoleweibo.widget.SwitchButton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

class SettingsGroupView extends LinearLayout {

    private LayoutInflater mInflater;

    public SettingsGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsGroupView(Context context) {
        super(context);
        init();
    }

    public void init() {
        setOrientation(LinearLayout.VERTICAL);
        mInflater = LayoutInflater.from(getContext());
    }

    public void addItems(ArrayList<SettingsItem> items) {
        if (null == items || 0 == items.size()) {
            return;
        }
        if (items.size() == 1) {
            addSingleItem(items.get(0));
        } else {
            addMultipleItem(items);
        }
    }

    public void addGroup(SettingsGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("SettingsGroup can't not be null");
        }
        addItems(group.getItems());
    }

    private void addSingleItem(SettingsItem item) {
        SettingsItem settingsItem = item;
        createAndAddItemView(R.drawable.selector_setting_item_bg_single, item);
    }

    private void addMultipleItem(ArrayList<SettingsItem> items) {
        SettingsItem item = null;
        for (int i = 0, len = items.size(); i < len; i++) {
            item = items.get(i);
            boolean needDivider = false;
            if (i == 0) {
                needDivider = true;
                createAndAddItemView(R.drawable.selector_setting_item_bg_top, item);
            } else if (i == (len - 1)) {
                createAndAddItemView(R.drawable.selector_setting_item_bg_bottom, item);
            } else {
                needDivider = true;
                createAndAddItemView(R.drawable.selector_setting_item_bg_normal, item);
            }
            if (needDivider) {
                createAndAddDivider();
            }
        }
    }

    private void createAndAddDivider() {
        mInflater.inflate(R.layout.tp_settings_group_divider, this);
    }

    private View createAndAddItemView(int backgroundResId, final SettingsItem item) {
        if (item == null || mInflater == null) {
            return null;
        }
        ViewGroup itemView = (ViewGroup)mInflater.inflate(R.layout.tp_settings_item, null);
        itemView.setBackgroundResource(backgroundResId);

        // populate ItemView from SettingsItem
        View itemContentView = item.getContentView(mInflater, itemView);
        itemView.addView(itemContentView, SettingsListView.LP_F_F);
        addView(itemView, SettingsListView.LP_F_W);

        final SwitchButton switchBtn = (SwitchButton)itemContentView.findViewById(R.id.switchbtn);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.onClick();
                switchBtn.toggle();
            }
        });

        return itemView;
    }

}
