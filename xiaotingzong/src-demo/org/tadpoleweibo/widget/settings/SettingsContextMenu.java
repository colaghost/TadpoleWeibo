
package org.tadpoleweibo.widget.settings;

import java.util.ArrayList;
import java.util.List;

import org.tadpole.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SettingsContextMenu extends Dialog implements View.OnClickListener, OnTouchListener {

    public interface ISettingMenuListener {
        public void onSettingContextMenuClick(int index);

        public void onSettingContextMenuDismiss();
    }

    public static final String TAG = "SettingComboMenu";

    private static final int RECOVERY_SELECTED_DELAY = 100;

    private LinearLayout mContainer;

    private List<View> mViewPool;

    private View mSelectedView;

    private Point mShowPos;

    private Handler mTouchHandler;

    private Runnable mRecoverySelectedExec;

    private ISettingMenuListener mListener;

    public SettingsContextMenu(Context context) {
        super(context, R.style.Tadpole_Dialog_ContextMenu);
        init(context);
    }

    private SettingsContextMenu(Context context, String[] options, int selectedIndex, int showPosX,
            int showPosY, ISettingMenuListener listener) {
        super(context, R.style.Tadpole_Dialog_ContextMenu);
        init(context);
        setPos(showPosX, showPosY);
        setOptions(options, selectedIndex, listener);
    }

    private void init(Context context) {
        mShowPos = new Point(0, 0);
        mViewPool = new ArrayList<View>();
        mContainer = new LinearLayout(context);
        mTouchHandler = new Handler();
        mContainer.setOrientation(LinearLayout.VERTICAL);
        mRecoverySelectedExec = new Runnable() {
            @Override
            public void run() {
                if (mSelectedView != null) {
                    mSelectedView.setSelected(true);
                }
            }
        };

        setContentView(mContainer);
        setCanceledOnTouchOutside(true);
        this.getWindow().setWindowAnimations(R.style.Tadpole_Dialog_ContextMenu_Anim);
    }

    private TextView createView(Context context, int num) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        TextView textView = null;
        for (int i = 0; i < num; i++) {
            textView = (TextView)layoutInflater.inflate(R.layout.tp_settings_contextmenu_item,
                    mContainer, false);
            textView.setBackgroundResource(R.drawable.tp_settings_contextmenu_bg_selector);
            // textView.setPadding(paddingLeft, 0, 0, 0);
            textView.setOnClickListener(this);
            textView.setOnTouchListener(this);
            mViewPool.add(textView);
        }
        return textView;
    }

    public void setPos(int x, int y) {
        mShowPos.x = x;
        mShowPos.y = y;
    }

    public void setOptions(String[] options, int selectedIndex, ISettingMenuListener listener) {
        mListener = listener;
        mViewPool.clear();
        mContainer.removeAllViews();

        TextView textView = null;
        if (options != null) {
            int optionCount = options.length;
            createView(this.getContext(), optionCount);
            for (int index = 0; index < optionCount; index++) {
                if (options[index].length() == 0) {
                    continue;
                }

                if (0 != index) {
                    View line = new View(getContext());
                    line.setBackgroundResource(R.drawable.tp_settings_contextmenu_item_line);
                    // 数字都是指1个像素
                    LayoutParams lp = new LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    lp.topMargin = 1;
                    lp.bottomMargin = 1;

                    mContainer.addView(line, lp);
                }

                textView = (TextView)mViewPool.get(index);
                if (index == selectedIndex) {
                    textView.setSelected(true);
                    mSelectedView = textView;
                }
                textView.setText(options[index]);
                textView.setTag(index);
                mContainer.addView(textView);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mSelectedView != null && mSelectedView != v) {
                mSelectedView.setSelected(false);
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mTouchHandler.postDelayed(mRecoverySelectedExec, RECOVERY_SELECTED_DELAY);
        }
        return false;
    }

    @Override
    protected void onStart() {
        int screenWidth = this.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = this.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        mContainer.measure(MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.AT_MOST));
        int width = mContainer.getMeasuredWidth();
        int height = mContainer.getMeasuredHeight();

        WindowManager.LayoutParams windowLayoutParams = this.getWindow().getAttributes();
        windowLayoutParams.x = (int)mShowPos.x - width;
        windowLayoutParams.y = (int)mShowPos.y;
        windowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        if (windowLayoutParams.y + height > screenHeight) {
            windowLayoutParams.y = screenHeight - height;
        }
    }

    @Override
    protected void onStop() {
        if (mListener != null) {
            mListener.onSettingContextMenuDismiss();
        }
    }

    @Override
    public void onClick(View v) {
        mSelectedView = v;
        this.dismiss();
        if (mListener != null) {
            mListener.onSettingContextMenuClick((Integer)v.getTag());
        }
    }

}
