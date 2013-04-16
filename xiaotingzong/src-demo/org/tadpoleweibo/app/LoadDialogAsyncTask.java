package org.tadpoleweibo.app;

import android.content.Context;
import android.os.AsyncTask;

public abstract class LoadDialogAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private LoadDialog mLoadDialog = null;

    public LoadDialogAsyncTask(Context context) {
        mLoadDialog = new LoadDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadDialog.show();
    }

    @Override
    protected abstract Result doInBackground(Params... params);

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        mLoadDialog.cancel();
    }
}
