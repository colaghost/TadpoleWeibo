
package org.tadpoleweibo.widget.image;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.NetStateManager;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.tadpoleweibo.app.TadpoleConfig;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.WeakHashMap;

public class ImageHelper {

    static final String TAG = "ImageHelper";

    private static ImageDiskCache cache = new ImageDiskCache(null);

    private static final String[] PROJECTIONS = {
            MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.LATITUDE,
            MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.TITLE
    };

    public Cursor getImage(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                PROJECTIONS, null, null, "");
        return cursor;
    }

    private static WeakHashMap<String, SoftReference<BitmapDrawable>> mCache = new WeakHashMap<String, SoftReference<BitmapDrawable>>();

    public static BitmapDrawable getBitmapDrawableFromCache(Context context, Uri uri) {
        String path = uri.getPath();
        if (mCache.containsKey(path)) {
            BitmapDrawable d = mCache.get(path).get();
            if (d != null) {
                if (TadpoleConfig.DEBUG) {
                    System.out.println("getBitmapDrawableFromCache path = " + path);
                }
                return d;
            }
        }
        return null;
    }

    public static BitmapDrawable getBitmapDrawable(Context context, Uri uri) {
        Bitmap b;
        try {
            String path = uri.getPath();
            if (mCache.containsKey(path)) {
                BitmapDrawable d = mCache.get(path).get();
                if (d != null) {
                    return d;
                } else {
                    mCache.remove(path);
                }
            }
            b = safeDecodeStream(context, uri, 60, 60);
            final BitmapDrawable bd = new BitmapDrawable(b);
            mCache.put(path, new SoftReference<BitmapDrawable>(bd));
            return bd;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A safer decodeStream method rather than the one of {@link BitmapFactory}
     * which will be easy to get OutOfMemory Exception while loading a big image
     * file.
     * 
     * @param uri
     * @param width
     * @param height
     * @return
     * @throws FileNotFoundException
     */
    protected static Bitmap safeDecodeStream(Context context, Uri uri, int width, int height)
            throws FileNotFoundException {
        int scale = 1;
        // Decode image size without loading all data into memory
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        android.content.ContentResolver resolver = context.getContentResolver();
        try {

            BitmapFactory.decodeStream(new BufferedInputStream(resolver.openInputStream(uri),
                    4 * 1024), null, options);
            if (width > 0 || height > 0) {
                options.inJustDecodeBounds = true;
                int w = options.outWidth;
                int h = options.outHeight;
                while (true) {
                    if ((width > 0 && w / 2 < width) || (height > 0 && h / 2 < height)) {
                        break;
                    }
                    w /= 2;
                    h /= 2;
                    scale *= 2;
                }
            }
            // Decode with inSampleSize option
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            return BitmapFactory.decodeStream(new BufferedInputStream(
                    resolver.openInputStream(uri), 4 * 1024), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
        }
        return null;
    }

    public static BitmapFactory.Options getBitmapOptions(Context context, Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ContentResolver resolver = context.getContentResolver();
        try {
            BitmapFactory.decodeStream(new BufferedInputStream(resolver.openInputStream(uri),
                    4 * 1024), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return options;
    }

    /**
     * A safer decodeStream method rather than the one of {@link BitmapFactory}
     * which will be easy to get OutOfMemory Exception while loading a big image
     * file.
     * 
     * @param uri
     * @param width
     * @param height
     * @return
     * @throws FileNotFoundException
     */
    protected static Bitmap safeDecodeStream2(Context context, Uri uri, int width, int height) {
        int scale = 1;
        // Decode image size without loading all data into memory
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        android.content.ContentResolver resolver = context.getContentResolver();
        try {

            BitmapFactory.decodeStream(new BufferedInputStream(resolver.openInputStream(uri),
                    4 * 1024), null, options);
            if (width > 0 || height > 0) {
                options.inJustDecodeBounds = true;
                int w = options.outWidth;
                int h = options.outHeight;
                if (TadpoleConfig.DEBUG) {
                    System.out.println("safeDecodeStream2 outWidth = " + w + ", outHeight = " + h);
                }
                int s = 1;

                int wScale = 1;
                int hScale = 1;

                if (w > width) {
                    wScale = w / width;
                }

                if (h > height) {
                    hScale = h / height;
                }

                if (wScale > hScale) {
                    s = wScale;
                } else {
                    s = hScale;
                }

                options.inSampleSize = s;
            }
            // Decode with inSampleSize option
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(new BufferedInputStream(
                    resolver.openInputStream(uri), 4 * 1024), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
        }
        return null;
    }

    public static BitmapDrawable getBitmapDrawableWithOutOOM(Context context, Uri uri) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Bitmap bitmap = safeDecodeStream2(context, uri, width, height);
        if (bitmap == null) {
            return null;
        } else {
            return new BitmapDrawable(context.getResources(), bitmap);
        }
    }

    public static BitmapDrawable getCacheBitmap(String url) {
        if (mCache.containsKey(url)) {
            BitmapDrawable d = mCache.get(url).get();
            return d;
        }
        return null;
    }

    public static BitmapDrawable getBitmapByUrl(Resources res, String url, ImageView imgView) {
        if (mCache.containsKey(url)) {
            BitmapDrawable d = mCache.get(url).get();
            if (d != null) {
                return d;
            } else {
                mCache.remove(url);
            }
        }
        final BitmapDrawable bd = getBitmapDrawable(res, url, imgView);
        mCache.put(url, new SoftReference<BitmapDrawable>(bd));
        return bd;
    }

    private static BitmapDrawable getBitmapDrawable(Resources res, String url, ImageView imgView) {
        String hash = url.hashCode() + "";
        try {
            byte[] data = null;
            if (cache.hasCache(hash)) {
                data = cache.readFromDisk(hash);
            } else {
                data = openUrl(url);
                cache.writeToDisk(hash, data);
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            if (imgView != null) {
                bitmap = roundCorners(bitmap, imgView, 10);
            }

            return new BitmapDrawable(res, bitmap);
        } catch (Exception e) {
            e.printStackTrace();

            cache.deleteFromDish(hash);
        }
        return null;
    }

    /**
     * @param url 服务器地址
     * @param method "GET"or “POST”
     * @return 响应结果
     * @throws WeiboException
     */
    public static byte[] openUrl(String url) throws Exception {
        byte[] result = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpUriRequest request = null;
            ByteArrayOutputStream bos = null;
            client.getParams()
                    .setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
            HttpGet get = new HttpGet(url);
            request = get;
            byte[] data = null;
            bos = new ByteArrayOutputStream();

            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();

            if (statusCode != 200) {
                result = readBytes(response);
                throw new Exception(new String(result));
            }
            result = readBytes(response);

            if (TadpoleConfig.DEBUG) {
                Log.d(TAG, "openUrl result = " + new String(result));
            }

            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    static byte[] readBytes(HttpResponse response) throws Exception {
        InputStream is = null;
        byte[] result = null;
        try {
            is = response.getEntity().getContent();
            byte[] buffer = new byte[1024];
            int byteRead = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((byteRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, byteRead);
            }
            result = baos.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
                is = null;
            }
        }
        return result;
    }

    /**
     * Process incoming {@linkplain Bitmap} to make rounded corners according to
     * target {@link ImageView}.<br />
     * This method <b>doesn't display</b> result bitmap in {@link ImageView}
     * 
     * @param bitmap Incoming Bitmap to process
     * @param imageView Target {@link ImageView} to display bitmap in
     * @param roundPixels
     * @return Result bitmap with rounded corners
     */
    public static Bitmap roundCorners(Bitmap bitmap, ImageView imageView, int roundPixels) {
        Bitmap roundBitmap;

        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int vw = imageView.getWidth();
        int vh = imageView.getHeight();
        if (vw <= 0)
            vw = bw;
        if (vh <= 0)
            vh = bh;

        int width, height;
        Rect srcRect;
        Rect destRect;
        switch (imageView.getScaleType()) {
            case CENTER_INSIDE:
                float vRation = (float)vw / vh;
                float bRation = (float)bw / bh;
                int destWidth;
                int destHeight;
                if (vRation > bRation) {
                    destHeight = Math.min(vh, bh);
                    destWidth = (int)(bw / ((float)bh / destHeight));
                } else {
                    destWidth = Math.min(vw, bw);
                    destHeight = (int)(bh / ((float)bw / destWidth));
                }
                int x = (vw - destWidth) / 2;
                int y = (vh - destHeight) / 2;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(x, y, x + destWidth, y + destHeight);
                width = vw;
                height = vh;
                break;
            case FIT_CENTER:
            case FIT_START:
            case FIT_END:
            default:
                vRation = (float)vw / vh;
                bRation = (float)bw / bh;
                if (vRation > bRation) {
                    width = (int)(bw / ((float)bh / vh));
                    height = vh;
                } else {
                    width = vw;
                    height = (int)(bh / ((float)bw / vw));
                }
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            case CENTER_CROP:
                vRation = (float)vw / vh;
                bRation = (float)bw / bh;
                int srcWidth;
                int srcHeight;
                if (vRation > bRation) {
                    srcWidth = bw;
                    srcHeight = (int)(vh * ((float)bw / vw));
                    x = 0;
                    y = (bh - srcHeight) / 2;
                } else {
                    srcWidth = (int)(vw * ((float)bh / vh));
                    srcHeight = bh;
                    x = (bw - srcWidth) / 2;
                    y = 0;
                }
                width = Math.min(vw, bw);
                height = Math.min(vh, bh);
                srcRect = new Rect(x, y, x + srcWidth, y + srcHeight);
                destRect = new Rect(0, 0, width, height);
                break;
            case FIT_XY:
                width = vw;
                height = vh;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            case CENTER:
            case MATRIX:
                width = Math.min(vw, bw);
                height = Math.min(vh, bh);
                x = (bw - width) / 2;
                y = (bh - height) / 2;
                srcRect = new Rect(x, y, x + width, y + height);
                destRect = new Rect(0, 0, width, height);
                break;
        }

        try {
            roundBitmap = getRoundedCornerBitmap(bitmap, roundPixels, srcRect, destRect, width,
                    height);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            roundBitmap = bitmap;
        }

        return roundBitmap;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixels, Rect srcRect,
            Rect destRect, int width, int height) {
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final RectF destRectF = new RectF(destRect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xFF000000);
        canvas.drawRoundRect(destRectF, roundPixels, roundPixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, destRectF, paint);

        return output;
    }
}
