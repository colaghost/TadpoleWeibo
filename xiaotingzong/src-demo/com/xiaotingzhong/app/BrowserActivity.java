/**
 *****************************************************************************
 * Copyright (C) 2005-2013 UCWEB Corporation. All rights reserved
 * File        : 2013-6-10
 *
 * Description : BrowserActivity.java
 *
 * Creation    : 2013-6-10
 * Author      : chenzh@ucweb.com
 * History     : Creation, 2013-6-10, chenzh, Create the file
 *****************************************************************************
 **/

package com.xiaotingzhong.app;

import org.tadpole.R;
import org.tadpoleweibo.view.TadpoleBrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Description
 * 
 * @author chenzh@ucweb.com
 * @version 1.0
 * @see
 */
public class BrowserActivity extends Activity {

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

    private TadpoleBrowser mTadpoleBrowser;

    private static final String KEY_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mTadpoleBrowser = (TadpoleBrowser)findViewById(R.id.browser);
        String url = getIntent().getStringExtra(KEY_URL);
        mTadpoleBrowser.loadUrl(url);
    }

}
