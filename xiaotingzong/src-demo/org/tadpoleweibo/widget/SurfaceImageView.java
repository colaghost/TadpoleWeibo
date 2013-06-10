
package org.tadpoleweibo.widget;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

import org.tadpole.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceImageView extends SurfaceView implements SurfaceHolder.Callback,
        AnimatorListener, ThreadListener {

    private boolean mCanRun = true;

    private static final String TAG = "SurfaceImageView";

    private static final boolean DEBUG = false;

    private static final int ANIMATIION_DURATION = 15000;

    private int mDrawableWidth = 0;

    private boolean mIsSurfaceValid = false;

    private Drawable mDrawable;

    private SurfaceHolder mHolder;

    private Matrix mMatrix = new Matrix();

    private MyThread mMyThread;

    private float mXOffset = 0.0F;

    public SurfaceImageView(Context context) {
        super(context);
        init();
    }

    public SurfaceImageView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public SurfaceImageView(Context context, AttributeSet attr, int style) {
        super(context, attr, style);
        init();
    }

    public float getXOffset() {
        return mXOffset;
    }

    public void setXOffset(float xOffset) {
        this.mXOffset = xOffset;
    }

    /**
     * 0 --> (mDrawableWidth - width) (mDrawableWidth - width) --> 0
     * instruction。
     * 
     * @param canvas
     */

    private void handlerDrawInternal(Canvas canvas) {
        if (DEBUG) {
            Log.d("SurfaceImageView", "handlerDrawInternal canvas = " + canvas + ", mDrawable = "
                    + mDrawable);
        }

        if (canvas == null || mDrawable == null) {
            return;
        }

        mMatrix.reset();
        mMatrix.postTranslate(-mXOffset, 0.0F);

        canvas.setMatrix(mMatrix);
        mDrawable.draw(canvas);
    }

    public void init() {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();

        mHolder = getHolder();
        mHolder.addCallback(this);
        mMyThread = new MyThread(mHolder);
        mDrawable = getContext().getResources().getDrawable(R.drawable.rootblock_default_bg);
    }

    public void setBitmapDrawable(BitmapDrawable drawable) {
        mDrawable = drawable;
    }

    public void startScroll() {
        if (!mIsSurfaceValid) {
            return;
        }

        mCanRun = true;
        if (!mMyThread.isAlive()) {
            mMyThread = new MyThread(mHolder);
            mMyThread.start(this);
        }
    }

    private void translateToRightAnimation() {
        ObjectAnimator a = ObjectAnimator.ofFloat(this, "xOffset", 0f,
                (float)(mDrawableWidth - mHolder.getSurfaceFrame().width()));
        if (DEBUG) {
            Log.d(TAG, "mDrawableWidth  - mHolder.getSurfaceFrame().width() = "
                    + (mDrawableWidth - mHolder.getSurfaceFrame().width()));
        }
        a.setRepeatMode(ObjectAnimator.REVERSE);
        a.setRepeatCount(ObjectAnimator.INFINITE);
        a.setDuration(ANIMATIION_DURATION);
        a.addListener(this);
        a.start();
    }

    public void stopScroll() {
        mCanRun = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.setFixedSize(width, height);
        Log.d("SurfaceImageView", "surfaceChanged width = " + holder.getSurfaceFrame().width()
                + ", height = " + holder.getSurfaceFrame().height());

    }

    public void surfaceCreated(SurfaceHolder holder) {
        mIsSurfaceValid = true;
        if (mDrawable != null) {
            // scale drawable to fit view height;
            int drawableHeight = mDrawable.getIntrinsicHeight();
            int drawableWidth = mDrawable.getIntrinsicWidth();
            int height = holder.getSurfaceFrame().height();
            mDrawableWidth = (int)(1.0f * height * drawableWidth / drawableHeight);
            mDrawable.setBounds(new Rect(0, 0, mDrawableWidth, height));
        }
        startScroll();
        if (DEBUG) {
            Log.d(TAG, "surfaceCreated width = " + holder.getSurfaceFrame().width() + ", height = "
                    + holder.getSurfaceFrame().height());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (DEBUG) {
            Log.d(TAG, "surfaceDestroyed width = " + holder.getSurfaceFrame().width()
                    + ", height = " + holder.getSurfaceFrame().height());
        }
        mIsSurfaceValid = false;
    }

    public void requestFrame() {
        System.out.println("requestFrame start ");
        Canvas canvas = null;
        try {
            if (!mCanRun) {
                return;
            }
            canvas = mHolder.lockCanvas();
            handlerDrawInternal(canvas);
            mHolder.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("requestFrame end ");
    }

    class MyThread extends Thread {

        private ThreadListener mCallback;

        Handler handler;

        private SurfaceHolder mHolder;

        public MyThread() {
        }

        public void run() {
            Looper.prepare();
            handler = new Handler(Looper.myLooper());
            if (mCallback != null) {
                mCallback.looperReady();
            }
            Looper.loop();
        }

        public synchronized void start(ThreadListener cb) {
            mCallback = cb;
            super.start();
        }

    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    private void startRefresh() {
        mMyThread.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("startRefresh == ");
                requestFrame();
                startRefresh();
            }
        }, 40);
    }

    @Override
    public void looperReady() {
        startRefresh();
        mMyThread.handler.post(new Runnable() {
            @Override
            public void run() {
                translateToRightAnimation();
            }
        });
    }
}
