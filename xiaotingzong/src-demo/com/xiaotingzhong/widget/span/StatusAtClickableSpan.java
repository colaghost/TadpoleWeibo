/**
 *****************************************************************************
 * Copyright (C) 2005-2013 UCWEB Corporation. All rights reserved
 * File        : 2013-6-10
 *
 * Description : WieboStatusClickableSpan.java
 *
 * Creation    : 2013-6-10
 * Author      : chenzh@ucweb.com
 * History     : Creation, 2013-6-10, chenzh, Create the file
 *****************************************************************************
 **/

package com.xiaotingzhong.widget.span;

import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.widget.WeiboStatusesListAdapter.ShowUserAsyncTask;

import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

/**
 * Description
 * 
 * @author chenzh@ucweb.com
 * @version 1.0
 * @see
 */
public class StatusAtClickableSpan extends ClickableSpan {
    
    public static final String TAG = "StatusAtClickableSpan";

    private String mMatch = null;

    public StatusAtClickableSpan(String match) {
        mMatch = match;
    }

    @Override
    public void onClick(View v) {
        if(mMatch == null) {
            return;
        }
        String screen_name = mMatch;
        screen_name = screen_name.replace("@", "");
        screen_name = screen_name.replace(":", "");
        screen_name = screen_name.trim();
        Log.d(TAG, "screen_name = " + screen_name);
        new ShowUserAsyncTask(v.getContext(), XTZApplication.getCurUser(), 0,
                screen_name).execute("");
    }

}
