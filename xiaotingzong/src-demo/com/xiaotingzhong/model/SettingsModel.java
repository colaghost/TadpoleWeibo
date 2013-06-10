/**
 *****************************************************************************
 * Copyright (C) 2005-2013 UCWEB Corporation. All rights reserved
 * File        : 2013-6-10
 *
 * Description : SettingsModel.java
 *
 * Creation    : 2013-6-10
 * Author      : chenzh@ucweb.com
 * History     : Creation, 2013-6-10, chenzh, Create the file
 *****************************************************************************
 **/
package com.xiaotingzhong.model;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.xiaotingzhong.app.XTZApplication;

/**
 * Description
 *
 * @author chenzh@ucweb.com
 * @version 1.0
 * @see
 */
public class SettingsModel {
    

    public static final int WEIBO_READ_MODE_DEFAULT = 1;
    
    public static final int WEIBO_READ_MODE_NO_IMAGE = 2;
    
    private static final String USE_INNER_BROWSER = "useInnerBrowser";
    
    private static final String WEIBO_READ_MODE = "weiboReadMode";
    
    
    
    private SharedPreferences mSharedPrefrences;
    
    public SettingsModel(SharedPreferences pref) {
        mSharedPrefrences = pref;
    }
    
    public void setUseInnerBrowser(boolean b){
        Editor editor = mSharedPrefrences.edit();
        editor.putBoolean(USE_INNER_BROWSER, b);
        editor.commit();
    }
    
    public boolean getUseInnerBrowser(){
        return mSharedPrefrences.getBoolean(USE_INNER_BROWSER, true);
    }
    
    /**
     *  获取微博阅读模式
     */
    public int getWeiboReadMode(){
        return mSharedPrefrences.getInt(WEIBO_READ_MODE, WEIBO_READ_MODE_DEFAULT);
    }
    
    
    /**
     *  设置微博阅读模式
     */
    public void setWeiboReadMode(int readMode){
        Editor editor = mSharedPrefrences.edit();
        editor.putInt(WEIBO_READ_MODE, readMode);
        editor.commit();
    }

}
