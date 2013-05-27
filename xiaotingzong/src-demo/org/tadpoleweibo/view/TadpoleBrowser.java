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

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Description
 * 
 * @author chenzh@ucweb.com
 * @version 1.0
 * @see
 */
public class TadpoleBrowser extends FrameLayout {

    private LayoutInflater mInflater = null;

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

    private void init() {
        mInflater = LayoutInflater.from(getContext());
    }

}
