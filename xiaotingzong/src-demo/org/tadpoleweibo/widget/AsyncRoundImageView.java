
package org.tadpoleweibo.widget;

import org.tadpole.R;
import org.tadpoleweibo.widget.image.ImageHelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncRoundImageView extends ImageView {
    static ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(5, 6, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(100), new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                }
            });

    private Future<?> mFuture;

    private boolean mDisableUsingImageUri = false;;

    private Uri mImageUri;

    private int mCornerRadius = 10;

    private Paint mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path mMaskPath;

    private Paint mTransparentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public AsyncRoundImageView(Context context) {
        super(context);
        init();
    }

    public AsyncRoundImageView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public AsyncRoundImageView(Context context, AttributeSet attr, int style) {
        super(context, attr, style);
        init();
    }

    private void init() {
        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null);
        this.mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mMaskPaint.setColor(Color.RED);
    }

    /**
     * 设置圆角半径
     */
    public void setCornerRadius(int r) {
        mCornerRadius = r;
    }

    public void setAsyncEnable(boolean b) {
        mDisableUsingImageUri = !b;
    }

    private void generateMaskPath(int width, int height) {
        this.mMaskPath = new Path();
        this.mMaskPath.addRoundRect(new RectF(0.0F, 0.0F, width, height), this.mCornerRadius,
                this.mCornerRadius, Path.Direction.CW);
        this.mMaskPath.setFillType(Path.FillType.INVERSE_WINDING);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if ((w != oldw) || (h != oldh))
            generateMaskPath(w, h);

    }

    protected void onDraw(Canvas canvas) {
        // 保存当前layer的透明橡树到离屏缓冲区。并新创建一个透明度爲255的新layer
        int saveCount = canvas.saveLayerAlpha(0.0F, 0.0F, canvas.getWidth(), canvas.getHeight(),
                255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        super.onDraw(canvas);
        if (this.mMaskPath != null) {
            canvas.drawPath(this.mMaskPath, this.mMaskPaint);
        }
        canvas.restoreToCount(saveCount);
    }

    @Override
    public void setImageURI(final Uri uri) {
        if (mDisableUsingImageUri) {
            super.setImageURI(uri);
            return;
        }

        final Uri curImageUri = mImageUri;
        final AsyncRoundImageView me = this;
        setImageResource(android.R.drawable.ic_menu_gallery);
        if (uri == null || (!uri.equals(curImageUri))) {

            if (curImageUri != null) {
                // cancel load
                final Future<?> future = mFuture;
                if (future != null) {
                    future.cancel(false);
                    mFuture = null;
                }
            }

            if (uri != null) {
                // load new
                mFuture = sExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("mFuture to thread pool");
                        final BitmapDrawable bd = ImageHelper.getBitmapDrawable(me.getContext(),
                                uri);

                        if (bd != null) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bd;
                            mHandler.sendMessage(msg);
                        }
                        mFuture = null;
                    }
                });
            }
        }
        mImageUri = uri;
    }

    public void setImageURL(final String url) {
        this.setImageResource(R.drawable.close_selector);
        final AsyncRoundImageView me = this;
        if (url != null) {

            BitmapDrawable bd = ImageHelper.getCacheBitmap(url);
            if (bd != null) {
                setImageDrawable(bd);
            } else {
                // load new
                mFuture = sExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        final BitmapDrawable bd = ImageHelper.getBitmapByUrl(me.getContext()
                                .getResources(), url, null);
                        if (bd != null) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bd;
                            mHandler.sendMessage(msg);
                        }
                        mFuture = null;
                    }
                });
            }
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                AsyncRoundImageView.this.setImageDrawable((BitmapDrawable)msg.obj);
                AsyncRoundImageView.this.startAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.alpha_in));
            }
            return false;
        }
    });

}
