
package com.xiaotingzhong.app.window;

import org.tadpoleweibo.framework.AbstractTpWindow;

import android.view.View;
import android.widget.TextView;

public class HelloWindow extends AbstractTpWindow {

    @Override
    public View onCreate() {

        getNavBar().setTitle("HelloWindow");

        return new TextView(getActivity());
    }

}
