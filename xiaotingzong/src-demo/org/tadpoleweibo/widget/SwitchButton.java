
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

    private boolean mIsTurnOn = false;

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
        mIsTurnOn = false;
    }

    public void setTurnOn(boolean isTurnOn) {
        mIsTurnOn = isTurnOn;
    }

    public void setSwitchListener(SwitchListener l) {
        mListener = l;
    }

    public void toggle() {
        switchAction(!mIsTurnOn);
    }

    public void turnOn() {
        switchAction(true);

    }

    public void turnOff() {
        switchAction(false);
    }

    private void notifyListener() {
        if (mListener == null) {
            return;
        }
        if (mIsTurnOn == true) {
            mListener.on();
        } else {
            mListener.off();
        }
    }

    private void switchAction(boolean b) {
        if (mIsTurnOn == b) {
            return;
        }
        mIsTurnOn = b;
        setImageByState();
        notifyListener();
    }

    private void setImageByState() {
        if (mIsTurnOn = false) {
            setImageResource(R.drawable.switch_left);
        } else {
            setImageResource(R.drawable.switch_right);
        }
    }

}
