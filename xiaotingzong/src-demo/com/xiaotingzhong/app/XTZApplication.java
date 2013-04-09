package com.xiaotingzhong.app;

import android.app.Application;
import android.content.res.Configuration;
import com.xiaotingzhong.app.storage.WeiboCache;

public class XTZApplication extends Application
{
  public static XTZApplication app;
  public static int curUid;
  public static WeiboCache sWeiboCache;

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
  }

  public void onCreate()
  {
    super.onCreate();
    app = this;
    sWeiboCache = new WeiboCache(this);
  }

  public void onLowMemory()
  {
    super.onLowMemory();
  }

  public void onTerminate()
  {
    super.onTerminate();
  }
}