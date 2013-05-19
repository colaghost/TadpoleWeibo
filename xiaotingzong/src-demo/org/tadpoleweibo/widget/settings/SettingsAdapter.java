
package org.tadpoleweibo.widget.settings;

import org.tadpole.R;
import org.tadpoleweibo.widget.BaseListAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SettingsAdapter extends BaseListAdapter<SettingsGroup, ListView> {

    public SettingsAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingsGroup item = getItemData(position);
        LinearLayout root = new LinearLayout(getLayoutInflater().getContext());
        root = (LinearLayout)getLayoutInflater().inflate(R.layout.tp_settings_group, root);

        SettingsGroupView view = (SettingsGroupView)root.findViewById(R.id.settingsgroupview);
        view.setGroup(item);
        return root;
    }
}
