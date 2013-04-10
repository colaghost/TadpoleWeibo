package org.tadpoleweibo.common;

import java.lang.reflect.Method;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ActivityUtil {

    private Activity mActivity;

    public ActivityUtil(Activity activity) {
        mActivity = activity;
    }

    public boolean isFullscreen() {
        int flags = mActivity.getWindow().getAttributes().flags;
        return ((flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public int getStatusbarHeight() {
        Rect rc = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rc);
        int screenHeight = getDisplayScreenHeight();
        int statusbarHeight = screenHeight - rc.height();
        return statusbarHeight;
    }

    public int getDisplayScreenHeight() {
        int screenHeight = 0;

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);

        int ver = Build.VERSION.SDK_INT;
        if (ver < 13) {
            screenHeight = metrics.heightPixels;
        } else if (ver == 13) {
            try {
                Method method = display.getClass().getMethod("getRealHeight");
                screenHeight = (Integer) method.invoke(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ver > 13) {
            try {
                Method method = display.getClass().getMethod("getRawHeight");
                screenHeight = (Integer) method.invoke(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return screenHeight;
    }
}
