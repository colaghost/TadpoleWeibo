
package org.tadpoleweibo.widget.settings;

import org.tadpole.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

public abstract class SettingsItem implements ISettingsItem {

    @Override
    public View getContentView(LayoutInflater inflater) {
        TextView view = new TextView(inflater.getContext());
        view.setText("打酱油测试");
        view.setLayoutParams(SettingsListView.LP_F_W);
        return view;
    }

    public void onClick() {
        // override me
        System.out.println("SettingsItem Must Be Override . Object = " + this);
    }

    public View getContentView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.tp_settings_item_content, null);
        view.setLayoutParams(SettingsListView.LP_F_F);

        ViewGroup viewGroupRight = (ViewGroup)view.findViewById(R.id.right);
        View viewRightDetail = createRightDetailView(inflater, viewGroupRight);
        if (null != viewRightDetail) {
            viewGroupRight.removeAllViews();
            viewGroupRight.addView(viewRightDetail, SettingsListView.LP_F_F);
        }
        return view;
    }

    public abstract View createRightDetailView(LayoutInflater inflater, ViewGroup parent);
}
