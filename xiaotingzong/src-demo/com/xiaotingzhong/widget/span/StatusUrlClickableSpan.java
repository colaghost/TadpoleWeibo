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
import com.xiaotingzhong.model.SettingsModel;

import android.content.Intent;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Description
 * 
 * @author chenzh@ucweb.com
 * @version 1.0
 * @see
 */
public class StatusUrlClickableSpan extends ClickableSpan {

    private String mMatch = null;

    public StatusUrlClickableSpan(String match) {
        mMatch = match;
    }

    @Override
    public void onClick(View v) {

        SettingsModel settingsModel = XTZApplication.getSettingsModel();
        boolean useInnerBrowser = settingsModel.getUseInnerBrowser();

        // 使用内置浏览器
        if (useInnerBrowser) {

        }
        // 使用外部浏览器
        else {
            Uri uri = Uri.parse(mMatch);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            v.getContext().startActivity(intent);
        }
    }

}
