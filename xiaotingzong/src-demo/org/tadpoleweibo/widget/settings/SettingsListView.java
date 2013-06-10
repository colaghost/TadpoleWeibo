
package org.tadpoleweibo.widget.settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SettingsListView extends ListView {

    static final LayoutParams LP_F_W = new LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT);

    public static final LayoutParams LP_F_F = new LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.FILL_PARENT);

    private SettingsAdapter mSettingsAdapter;

    public SettingsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SettingsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setSelector(new ColorDrawable(Color.TRANSPARENT));
        setDivider(null);
        setDividerHeight(0);
        setFocusableInTouchMode(false);
        this.setVerticalScrollBarEnabled(false);
        setAdapter(new SettingsAdapter(getActivity()));
    }

    public void addGroup(SettingsGroup group) {
        mSettingsAdapter.addItem(group);
        mSettingsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof SettingsAdapter) {
            mSettingsAdapter = (SettingsAdapter)adapter;
        } else {
            throw new IllegalArgumentException("SettingView Adapter must be SettingAdapter");
        }
    }

    private Activity getActivity() {
        return (Activity)getContext();
    }

    public void notiyDataSetChanged() {
        mSettingsAdapter.notifyDataSetChanged();
    }
}
