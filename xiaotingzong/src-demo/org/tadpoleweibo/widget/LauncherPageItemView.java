
package org.tadpoleweibo.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

public class LauncherPageItemView extends LinearLayout {

    private static final String TAG = "LaucherItemView";

    private AnimationListener mAniListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (mAnimationListener != null) {
                mAnimationListener.onAnimationStart(animation);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            if (mAnimationListener != null) {
                mAnimationListener.onAnimationRepeat(animation);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mAnimationListener != null) {
                mAnimationListener.onAnimationEnd(animation);
            }
            LauncherPageItemView.this.clearAnimation();
            if (mRunnable != null) {
                mRunnable.run();
                mRunnable = null;
            }
        }
    };

    public LauncherPageItemView(Context context) {
        super(context);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    private Runnable mRunnable = null;

    public void setEndRunnable(Runnable runable) {
        this.mRunnable = runable;
    }

    public void forceAniEnd() {
        if (mRunnable != null) {
            mRunnable.run();
            mRunnable = null;
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

    private AnimationListener mAnimationListener;

    public void setAnimationListener(AnimationListener listener) {
        mAnimationListener = listener;
    }
}
