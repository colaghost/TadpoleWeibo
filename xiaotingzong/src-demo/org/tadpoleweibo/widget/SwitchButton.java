
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
        setTurnOn(false);
        setImageByState();
    }

    public boolean setTurnOn(boolean isTurnOn) {
        if (isTurnOn == mIsTurnOn) {
            return false;
        }
        mIsTurnOn = isTurnOn;
        setImageByState();
        return true;
    }

    public boolean isTurnOn() {
        return mIsTurnOn;
    }

    public void setSwitchListener(SwitchListener l) {
        mListener = l;
    }

    public void toggle() {
        boolean b = !mIsTurnOn;
        switchAction(b);
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
        if (isTurnOn()) {
            mListener.on();
        } else {
            mListener.off();
        }
    }

    private void switchAction(boolean b) {
        if (setTurnOn(b)) {
            return;
        }
        notifyListener();
    }

    private void setImageByState() {
        if (isTurnOn()) {
            setImageResource(R.drawable.switch_on);
        } else {
            setImageResource(R.drawable.switch_off);
        }
    }

}
