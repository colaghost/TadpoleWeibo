package org.tadpoleweibo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

public class LaucherItemView extends LinearLayout {

    private static final String TAG = "LaucherItemView";

    private AnimationListener mAniListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            LaucherItemView.this.clearAnimation();
            if (runnable != null) {
                runnable.run();
                runnable = null;
            }
        }
    };

    public LaucherItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LaucherItemView(Context context) {
        super(context);
    }

    private Runnable runnable = null;

    public void setEndRunnable(Runnable runable) {
        this.runnable = runable;
    }

    public void forceAniEnd() {
        if (runnable != null) {
            runnable.run();
            runnable = null;
        }
    }

    @Override
    public void startAnimation(Animation animation) {
        super.startAnimation(animation);
        animation.setAnimationListener(mAniListener);
    }

    @Override
    public void clearAnimation() {
        super.clearAnimation();
    }
}
