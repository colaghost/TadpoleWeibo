package org.tadpoleweibo.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class LaucherItemView extends TextView {

    private ArrayList<Runnable> mRunnableList = new ArrayList<Runnable>();

    public LaucherItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LaucherItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LaucherItemView(Context context) {
        super(context);
    }

}
