
package org.tadpoleweibo.widget.settings;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsMenuDialog extends Dialog implements android.view.View.OnClickListener {

    public static class SettngsMenuItem {
        public String text = "";
    }

    private ArrayList<MenuItem> mItemList = new ArrayList<MenuItem>();

    static final LayoutParams LP_W_W = new LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);

    private LinearLayout mContentView = null;

    public SettingsMenuDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public SettingsMenuDialog(Context context) {
        super(context);
        init();
    }

    public SettingsMenuDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public void addMenuItem(String title) {
        TextView txtView = new TextView(getContext());
        txtView.setText(title);
        mContentView.addView(txtView, LP_W_W);
        txtView.setOnClickListener(this);
    }

    private void init() {
        mContentView = new LinearLayout(getContext());
        mContentView.setLayoutParams(LP_W_W);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        this.setContentView(mContentView);
    }

    @Override
    public void onClick(View arg0) {
    }
}
