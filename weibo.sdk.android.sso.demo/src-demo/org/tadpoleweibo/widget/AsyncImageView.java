package org.tadpoleweibo.widget;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.tadpole.R;
import org.tadpoleweibo.widget.image.ImageHelper;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AsyncImageView extends ImageView {
    static ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(5, 6, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        }
    });


    private Future<?> mFuture;

    private boolean mDisableUsingImageUri = false;;
    private Uri mImageUri;


    public void setAsyncEnable(boolean b) {
        mDisableUsingImageUri = !b;
    }

    public AsyncImageView(Context context) {
        super(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageURI(final Uri uri) {
        if (mDisableUsingImageUri) {
            super.setImageURI(uri);
            return;
        }

        final Uri curImageUri = mImageUri;
        final AsyncImageView me = this;
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
                        final BitmapDrawable bd = ImageHelper.getBitmapDrawable(me.getContext(), uri);
                        System.out.println("mFuture to thread pool) = " + bd);

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

    public void setImageURL(final String profile_image_url) {
        final AsyncImageView me = this;
        if (profile_image_url != null) {

            BitmapDrawable bd = ImageHelper.getCacheBitmap(profile_image_url);
            if (bd != null) {
                setImageDrawable(bd);
            } else {
                // load new
                mFuture = sExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        final BitmapDrawable bd = ImageHelper.getBitmapByUrl(me.getContext().getResources(), profile_image_url);
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
                AsyncImageView.this.setImageDrawable((BitmapDrawable) msg.obj);
                AsyncImageView.this.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha_in));
            }
            return false;
        }
    });



}
