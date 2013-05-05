
package com.xiaotingzhong.app;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * A base Activity can control App All activity to do something like test
 * functions <br>=
 * ========================= <br>
 * author：Zenip <br>
 * email：lxyczh@gmail.com <br>
 * create：2013-5-4 <br>=
 * =========================
 */
public class BaseActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("hello world");
        return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    
    
    
}
