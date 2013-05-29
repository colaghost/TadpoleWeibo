/**
 *****************************************************************************
 * Copyright (C) 2005-2013 UCWEB Corporation. All rights reserved
 * File        : 2013-5-27
 *
 * Description : TadpoleWebview.java
 *
 * Creation    : 2013-5-27
 * Author      : chenzh@ucweb.com
 * History     : Creation, 2013-5-27, chenzh, Create the file
 *****************************************************************************
 **/

package org.tadpoleweibo.view;

import org.tadpole.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 * Description
 * 
 * @author chenzh@ucweb.com
 * @version 1.0
 * @see
 */
public class TadpoleBrowser extends FrameLayout implements OnClickListener {

    private LayoutInflater mInflater = null;

    private ViewGroup mViewGroup = null;

    private View mBack = null;

    private View mPrev = null;

    private View mForward = null;

    private View mRefresh = null;

    private TadpoleWebView mWebview;

    public TadpoleBrowser(Context context) {
        super(context);
        init();
    }

    public TadpoleBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TadpoleBrowser(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    public WebView getWebView(){
        return mWebview;
    }

    public void loadUrl(String url) {
        mWebview.loadUrl(url);
    }

    private void init() {
        mInflater = LayoutInflater.from(getContext());
        mViewGroup = (ViewGroup)mInflater.inflate(R.layout.tp_browser, this, false);
        this.addView(mViewGroup);

        mBack = mViewGroup.findViewById(R.id.back);
        mBack.setOnClickListener(this);

        mPrev = mViewGroup.findViewById(R.id.previous);
        mPrev.setOnClickListener(this);

        mForward = mViewGroup.findViewById(R.id.forward);
        mForward.setOnClickListener(this);

        mRefresh = mViewGroup.findViewById(R.id.refresh);
        mRefresh.setOnClickListener(this);

        mWebview = (TadpoleWebView)mViewGroup.findViewById(R.id.webview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ((Activity)getContext()).finish();
                break;
            case R.id.previous:
                mWebview.goBack();
                break;
            case R.id.forward:
                mWebview.goForward();
                break;
            case R.id.refresh:
                mWebview.reload();
                break;
            default:
                break;
        }
    }

}
