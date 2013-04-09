package com.xiaotingzhong.app.storage;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONObject;
import org.tadpoleweibo.common.FileUtil;

public class SubscriptionMgr extends BaseUserFileCache
{
  static final String SUBSCRIPT_DIR = "subscript";
  private File mSubscriptDir = null;

  public SubscriptionMgr(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }

  public ArrayList<Integer> getSubscriptedUids()
  {
    ArrayList localArrayList = new ArrayList();
    File[] arrayOfFile = this.mSubscriptDir.listFiles();
    int i = arrayOfFile.length;
    for (int j = 0; ; j++)
    {
      if (j >= i)
        return localArrayList;
      localArrayList.add(Integer.valueOf(Integer.valueOf(arrayOfFile[j].getName()).intValue()));
    }
  }

  public boolean isSubscripted(int paramInt)
  {
    return new File(this.mSubscriptDir.getAbsolutePath() + File.separator + paramInt).exists();
  }

  public void subscript(int paramInt)
  {
    SubsriptItem localSubsriptItem = new SubsriptItem();
    FileUtil.writeFile(this.mSubscriptDir.getAbsolutePath() + File.separator + paramInt, localSubsriptItem.toJSONBytes(), false);
  }

  public void unSubscript(int paramInt)
  {
    FileUtil.delete(this.mSubscriptDir.getAbsolutePath() + File.separator + paramInt);
  }

  public static final class SubsriptItem
  {
    public byte[] toJSONBytes()
    {
      return toJSONObject().toString().getBytes();
    }

    public JSONObject toJSONObject()
    {
      return new JSONObject();
    }

    public String toJSONString()
    {
      return toJSONObject().toString();
    }
  }
}