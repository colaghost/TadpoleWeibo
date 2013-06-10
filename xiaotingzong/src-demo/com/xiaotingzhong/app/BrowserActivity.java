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
import android.os.Bundle;

/**
 * Description
 *
 * @author chenzh@ucweb.com
 * @version 1.0
 * @see
 */
public class BrowserActivity extends Activity{
    
    private TadpoleBrowser mTadpoleBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mTadpoleBrowser = (TadpoleBrowser)findViewById(R.id.browser);
    }
    
}
