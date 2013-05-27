
package org.tadpoleweibo.widget.settings;

import java.util.ArrayList;

public class SettingsGroup {
    private ArrayList<SettingsItem> mItems = new ArrayList<SettingsItem>();

    private String mTitle;

    public void addItem(SettingsItem item) {
        mItems.add(item);
    }

    public ArrayList<SettingsItem> getItems() {
        return mItems;
    }

    public void setTitle(String t) {
        mTitle = t;
    }

    public String getTitle() {
        return mTitle;
    }
}
