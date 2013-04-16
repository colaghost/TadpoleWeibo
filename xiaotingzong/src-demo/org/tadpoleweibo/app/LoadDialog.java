package org.tadpoleweibo.app;

import org.tadpole.R;

import android.app.Dialog;
import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadDialog extends Dialog {

    private ImageView mImgViewRefresh;

    public LoadDialog(Context context) {
        super(context, R.style.Tadpole_Dialog_Loading);
        setContentView(R.layout.tp_dialog_loading);
        mImgViewRefresh = (ImageView) findViewById(R.id.imgview_loadrefresh);
    }

    @Override
    public void show() {
        super.show();
        mImgViewRefresh.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_self));
    }

}
