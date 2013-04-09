package org.tadpoleweibo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class SurfaceImageView extends SurfaceView implements SurfaceHolder.Callback {
    static final String TAG = "MarqueeTextSurfaceView";
    private int drawableWidth = 0;
    private boolean forward = true;
    private boolean isSurfaceValid = false;
    private Drawable mDrawable;
    private SurfaceHolder mHolder;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix matrix = new Matrix();
    private MyThread myThread;
    private float xOffset = 0.0F;

    public SurfaceImageView(Context paramContext) {
        super(paramContext);
        init();
    }

    public SurfaceImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public SurfaceImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void handlerDrawInternal(Canvas paramCanvas) {
        if (paramCanvas == null)
            ;
        int i;
        do {
            return;
            i = this.mHolder.getSurfaceFrame().width();
        } while (this.mDrawable == null);
        this.matrix.reset();
        if (this.forward) {
            this.xOffset = (0.8F + this.xOffset);
            if (this.xOffset > this.drawableWidth - i)
                this.forward = false;
        }
        while (true) {
            this.matrix.postTranslate(-this.xOffset, 0.0F);
            paramCanvas.setMatrix(this.matrix);
            this.mDrawable.draw(paramCanvas);
            break;
            this.xOffset -= 0.8F;
            if (this.xOffset >= 0.0F)
                continue;
            this.forward = true;
        }
    }

    public void init() {
        this.mDrawable = getContext().getResources().getDrawable(2130837537);
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.myThread = new MyThread(this.mHolder);
        this.mHolder.setFormat(-2);
    }

    public void setBitmapDrawable(BitmapDrawable paramBitmapDrawable) {
        this.mDrawable = paramBitmapDrawable;
    }

    public void startScroll() {
        if (!this.isSurfaceValid)
            ;
        while (true) {
            return;
            this.myThread.canRun = true;
            if (this.myThread.isAlive())
                continue;
            this.myThread = new MyThread(this.mHolder);
            this.myThread.start();
        }
    }

    public void stopScroll() {
        this.myThread.canRun = false;
    }

    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {
        Log.d("MarqueeTextSurfaceView", "surfaceChanged");
        paramSurfaceHolder.setFixedSize(paramInt2, paramInt3);
    }

    public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
        Log.d("MarqueeTextSurfaceView", "surfaceCreated");
        this.isSurfaceValid = true;
        startScroll();
        if (this.mDrawable != null) {
            this.drawableWidth = this.mDrawable.getIntrinsicWidth();
            this.mDrawable.setBounds(new Rect(0, 0, this.drawableWidth, paramSurfaceHolder.getSurfaceFrame().height()));
        }
    }

    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
        Log.d("MarqueeTextSurfaceView", "surfaceDestroyed");
        this.isSurfaceValid = false;
    }

    class MyThread extends Thread {
        public boolean canRun = true;
        private SurfaceHolder holder;

        public MyThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        public void run() {
            Canvas localCanvas = null;
            while (true) {
                if ((!this.canRun) || (!SurfaceImageView.this.isSurfaceValid))
                    ;
                try {
                    SurfaceImageView.this.xOffset = 0.0F;
                    localCanvas = this.holder.lockCanvas();
                    SurfaceImageView.this.handlerDrawInternal(localCanvas);
                    try {
                        localCanvas = this.holder.lockCanvas();
                        SurfaceImageView.this.handlerDrawInternal(localCanvas);
                        if (localCanvas != null)
                            this.holder.unlockCanvasAndPost(localCanvas);
                        try {
                            Thread.sleep(40L);
                        } catch (InterruptedException localInterruptedException) {
                            localInterruptedException.printStackTrace();
                        }
                        continue;
                    } catch (Exception localException2) {
                        while (true) {
                            localException2.printStackTrace();
                            if (localCanvas == null)
                                continue;
                            this.holder.unlockCanvasAndPost(localCanvas);
                        }
                    } finally {
                        if (localCanvas != null)
                            this.holder.unlockCanvasAndPost(localCanvas);
                    }
                } catch (Exception localException1) {
                    while (true) {
                        localException1.printStackTrace();
                        if (localCanvas == null)
                            continue;
                        this.holder.unlockCanvasAndPost(localCanvas);
                    }
                } finally {
                    if (localCanvas != null)
                        this.holder.unlockCanvasAndPost(localCanvas);
                }
            }
        }
    }
}