
package org.tadpoleweibo.widget;

import org.tadpole.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SwitchButton extends ImageView {
    public interface SwitchListener {
        void on();

        void off();
    }

    private static final int TURN_ON = 1;

    private static final int TURN_OFF = 2;

    private int mState = TURN_ON;

    private SwitchListener mListener = null;

    public SwitchButton(Context context) {
        super(context);
        init();
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setScaleType(ScaleType.FIT_END);
        setImageResource(R.drawable.switch_left);
        setDefaultTurnOff();
    }

    public void setDefaultTurnOn() {
        mState = TURN_ON;
    }

    public void setDefaultTurnOff() {
        mState = TURN_OFF;
    }

    public void setSwitchListener(SwitchListener l) {
        mListener = l;
    }

    public void toggle() {
        if (TURN_OFF == mState) {
            mState = TURN_ON;
            setImageResource(R.drawable.switch_right);
            // fire listener;
        } else {
            mState = TURN_OFF;
            setImageResource(R.drawable.switch_left);
        }

        if (mListener == null) {
            return;
        }

        if (TURN_ON == mState) {
            mListener.on();
        } else {
            mListener.off();
        }

    }
}
