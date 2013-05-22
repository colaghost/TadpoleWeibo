package org.tadpoleweibo.widget;

import org.tadpole.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceImageView extends SurfaceView implements SurfaceHolder.Callback {
    static final String TAG = "MarqueeTextSurfaceView";
    private int mDrawableWidth = 0;
    private boolean mForward = true;
    private boolean mIsSurfaceValid = false;
    private Drawable mDrawable;
    private SurfaceHolder mHolder;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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

    private void handlerDrawInternal(Canvas canvas) {
        //        Log.d("SurfaceImageView", "handlerDrawInternal canvas = " + canvas + ", mDrawable = " + mDrawable);
        if (canvas == null || mDrawable == null) {
            return;
        }
        int width = mHolder.getSurfaceFrame().width();
        mMatrix.reset();
        if (mForward) {
            mXOffset += 0.8f;
            if (mXOffset > (mDrawableWidth - width)) {
                mForward = false;
            }
        } else {
            mXOffset -= 0.8f;
            if (mXOffset < 0) {
                mForward = true;
            }
        }
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
        if (!mIsSurfaceValid){
            return;
        }
        mMyThread.mCanRun = true;
        if (!mMyThread.isAlive()) {
            mMyThread = new MyThread(mHolder);
            mMyThread.start();
        }

    }

    public void stopScroll() {
        mMyThread.mCanRun = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.setFixedSize(width, height);
        Log.d("SurfaceImageView", "surfaceChanged width = " + holder.getSurfaceFrame().width() + ", height = " + holder.getSurfaceFrame().height());

    }

    public void surfaceCreated(SurfaceHolder holder) {
        mIsSurfaceValid = true;
        if (mDrawable != null) {
            // scale drawable to fit view height;
            int drawableHeight = mDrawable.getIntrinsicHeight();
            int drawableWidth = mDrawable.getIntrinsicWidth();
            int height = holder.getSurfaceFrame().height();
            mDrawableWidth = (int) (1.0f * height * drawableWidth / drawableHeight);
            mDrawable.setBounds(new Rect(0, 0, mDrawableWidth, height));
        }
        startScroll();
        Log.d("SurfaceImageView", "surfaceCreated width = " + holder.getSurfaceFrame().width() + ", height = " + holder.getSurfaceFrame().height());
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("SurfaceImageView", "surfaceDestroyed width = " + holder.getSurfaceFrame().width() + ", height = " + holder.getSurfaceFrame().height());
        mIsSurfaceValid = false;
    }

    class MyThread extends Thread {
        public boolean mCanRun = true;
        private SurfaceHolder mHolder;

        public MyThread(SurfaceHolder holder) {
            mHolder = holder;
        }

        public void run() {
            Canvas canvas = null;
            while (mIsSurfaceValid) {
                if (!mCanRun) {
                    continue;
                }
                try {
                    canvas = mHolder.lockCanvas();
                    handlerDrawInternal(canvas);
                    mHolder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}