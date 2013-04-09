package org.tadpoleweibo.widget;

import org.tadpole.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceImageView extends SurfaceView implements SurfaceHolder.Callback {
    static final String TAG = "MarqueeTextSurfaceView";
    private int mDrawableWidth = 0;
    private boolean forward = true;
    private boolean isSurfaceValid = false;
    private Drawable mDrawable;
    private SurfaceHolder mHolder;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix matrix = new Matrix();
    private MyThread myThread;
    private float xOffset = 0.0F;

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
        if (canvas == null || mDrawable == null) {
            return;
        }
        int width = mHolder.getSurfaceFrame().width();
        matrix.reset();
        if (forward) {
            xOffset += 0.8f;
            if (xOffset > (mDrawableWidth - width)) {
                forward = false;
            }
        } else {
            xOffset -= 0.8f;
            if (xOffset < 0) {
                forward = true;
            }
        }
        matrix.postTranslate(-xOffset, 0.0F);
        canvas.setMatrix(matrix);
        mDrawable.draw(canvas);
    }

    public void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        myThread = new MyThread(mHolder);
        mDrawable = getContext().getResources().getDrawable(R.drawable.rootblock_default_bg);
    }

    public void setBitmapDrawable(BitmapDrawable drawable) {
        mDrawable = drawable;
    }

    public void startScroll() {
        if (!isSurfaceValid)
            return;
        myThread.canRun = true;
        if (!myThread.isAlive()) {
            myThread = new MyThread(mHolder);
            myThread.start();
        }

    }

    public void stopScroll() {
        myThread.canRun = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("MarqueeTextSurfaceView", "surfaceChanged");
        holder.setFixedSize(width, height);

    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("MarqueeTextSurfaceView", "surfaceCreated");
        isSurfaceValid = true;
        startScroll();
        if (mDrawable != null) {
            // scale drawable to fit view height;
            int drawableHeight = mDrawable.getIntrinsicHeight();
            int drawableWidth = mDrawable.getIntrinsicWidth();
            int height = holder.getSurfaceFrame().height();
            mDrawableWidth = (int) (1.0f * height * drawableWidth / drawableHeight);
            mDrawable.setBounds(new Rect(0, 0, mDrawableWidth, height));
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("MarqueeTextSurfaceView", "surfaceDestroyed");
        isSurfaceValid = false;
    }

    class MyThread extends Thread {
        public boolean canRun = true;
        private SurfaceHolder mHolder;

        public MyThread(SurfaceHolder holder) {
            mHolder = holder;
        }

        public void run() {
            Canvas canvas = null;
            while (canRun && isSurfaceValid) {
                canvas = mHolder.lockCanvas();
                handlerDrawInternal(canvas);
                mHolder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}