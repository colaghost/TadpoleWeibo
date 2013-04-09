package com.xiaotingzhong.app.storage;

import android.content.Context;
import java.io.File;
import org.tadpoleweibo.common.FileUtil;

public class BaseUserFileCache
{
  private String mCachePath;

  public BaseUserFileCache(Context paramContext, int paramInt)
  {
    this.mCachePath = (paramContext.getFilesDir() + File.separator + paramInt + File.separator);
    FileUtil.createDir(this.mCachePath);
  }

  public File getSubDir(String paramString)
  {
    String str = this.mCachePath + paramString + File.separator;
    FileUtil.createDir(str);
    return new File(str);
  }

  public File getSubFile(String paramString)
  {
    String str = this.mCachePath + paramString;
    FileUtil.createFile(str);
    return new File(str);
  }
}