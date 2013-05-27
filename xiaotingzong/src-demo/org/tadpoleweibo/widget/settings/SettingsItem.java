
package org.tadpoleweibo.widget.settings;

import org.tadpole.R;
import org.tadpoleweibo.common.StringUtil;
import org.tadpoleweibo.common.TLog;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class SettingsItem<T> {

    public static final String TAG = "SettingsItem";

    private ViewGroup mBackground;

    public interface SettingsItemListener<T> {
        public void onSettingsAction(SettingsItem<T> item, T params);
    }

    private Object mTag = null;

    private String mTitle = "";

    private String mSummary = "";

    private SettingsItemListener<T> mListener;

    public SettingsItem(String title, String summary) {
        mTitle = title;
        mSummary = summary;
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    public Object getTag() {
        return mTag;
    }

    public void setListener(SettingsItemListener<T> l) {
        mListener = l;
    }

    public void onItemClick(ViewGroup itemRoot) {
        // override me
        System.out.println("SettingsItem Must Be Override . Object = " + this);
    }

    protected void setBackgroundSelected(final boolean b) {
//        mBackground.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mBackground.setPressed(b);
//            }
//        }, 100);
    }

    protected void notifyListener(T params) {
        mListener.onSettingsAction(this, params);
        TLog.debug(TAG, "notifyListener params = " + params);
    }

    View getItemView(LayoutInflater inflater, ViewGroup parent) {
        mBackground = (ViewGroup)inflater.inflate(R.layout.tp_settings_item, parent, false);
        getContentView(inflater, mBackground);
        return mBackground;
    }

    View getContentView(LayoutInflater inflater, ViewGroup parent) {

        View view = inflater.inflate(R.layout.tp_settings_item_content, parent, true);
        // view.setLayoutParams(SettingsListView.LP_F_F);

        TextView txtViewTitle = (TextView)view.findViewById(R.id.txtview_title);
        TextView txtViewSummary = (TextView)view.findViewById(R.id.txtview_summary);

        txtViewTitle.setText(mTitle);

        if (StringUtil.isBlank(mSummary)) {
            txtViewSummary.setVisibility(View.GONE);
        } else {
            txtViewSummary.setText(mSummary);
        }

        ViewGroup viewGroupRight = (ViewGroup)view.findViewById(R.id.right);
        View viewRightDetail = createRightDetailView(inflater, viewGroupRight);
        if (null != viewRightDetail) {
            viewGroupRight.removeAllViews();
            viewGroupRight.addView(viewRightDetail);
        }
        return view;
    }

    public abstract View createRightDetailView(LayoutInflater inflater, ViewGroup parent);
}
