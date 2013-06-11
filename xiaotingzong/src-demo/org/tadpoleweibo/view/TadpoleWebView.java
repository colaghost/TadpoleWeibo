
package org.tadpoleweibo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;

public class TadpoleWebView extends WebView {

    public TadpoleWebView(Context context) {
        super(context);
        init();
    }

    public TadpoleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TadpoleWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        WebSettings settings = getSettings();
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true); // 显示放大缩小 controler
        settings.setSupportZoom(true); // 可以缩放
        settings.setDefaultZoom(ZoomDensity.CLOSE);

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setInitialScale(100);
    }

}
