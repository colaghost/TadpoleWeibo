package com.xiaotingzhong.app;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class MoveBackground {
    private Animation mAniMoveToLeft;
    private Animation mAniMoveToRight;
    private View mRunImage;

    public MoveBackground(View paramView) {
        this.mRunImage = paramView;
    }

    public void startMove() {
        this.mAniMoveToRight = new TranslateAnimation(2, 0.0F, 2, -1.0F, 2, 0.0F, 2, 0.0F);
        this.mAniMoveToLeft = new TranslateAnimation(2, -1.0F, 2, 0.0F, 2, 0.0F, 2, 0.0F);
        this.mAniMoveToRight.setDuration(30000L);
        this.mAniMoveToLeft.setDuration(30000L);
        this.mAniMoveToRight.setFillAfter(true);
        this.mAniMoveToLeft.setFillAfter(true);
        this.mAniMoveToRight.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation paramAnimation) {
                MoveBackground.this.mRunImage.startAnimation(MoveBackground.this.mAniMoveToLeft);
            }

            public void onAnimationRepeat(Animation paramAnimation) {
            }

            public void onAnimationStart(Animation paramAnimation) {
            }
        });
        this.mAniMoveToLeft.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation paramAnimation) {
                MoveBackground.this.mRunImage.startAnimation(MoveBackground.this.mAniMoveToRight);
            }

            public void onAnimationRepeat(Animation paramAnimation) {
            }

            public void onAnimationStart(Animation paramAnimation) {
            }
        });
        this.mRunImage.startAnimation(this.mAniMoveToRight);
    }
}