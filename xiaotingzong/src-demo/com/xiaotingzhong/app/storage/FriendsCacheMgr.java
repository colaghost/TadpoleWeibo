package com.xiaotingzhong.app.storage;

import android.content.Context;
import com.weibo.sdk.android.api.response.User;
import com.weibo.sdk.android.api.response.builder.UserBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tadpoleweibo.common.FileUtil;
import org.tadpoleweibo.widget.PageList;

public class FriendsCacheMgr extends BaseUserFileCache
{
  private File mFriendsDir = getSubDir("friends" + File.separator);

  public FriendsCacheMgr(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }

  public ArrayList<User> getFriendsByUids(ArrayList<Integer> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramArrayList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return localArrayList;
      int i = ((Integer)localIterator.next()).intValue();
      String str = this.mFriendsDir.getAbsolutePath() + File.separator + i;
      Object localObject = null;
      try
      {
        User localUser = UserBuilder.fromResponse(FileUtil.readFile(new File(str)));
        localObject = localUser;
        if (localObject == null)
          continue;
        localArrayList.add(localObject);
      }
      catch (IOException localIOException)
      {
        while (true)
          localIOException.printStackTrace();
      }
    }
  }

  public PageList<User> getFriendsFromCache()
    throws Exception
  {
    ArrayList localArrayList = new ArrayList();
    File[] arrayOfFile = this.mFriendsDir.listFiles();
    int i = arrayOfFile.length;
    for (int j = 0; ; j++)
    {
      if (j >= i)
      {
        PageList localPageList = new PageList();
        localPageList.records = localArrayList;
        localPageList.totalCount = localArrayList.size();
        localPageList.nextStartIndex = 0;
        localPageList.prevPage = 0;
        return localPageList;
      }
      localArrayList.add(UserBuilder.fromResponse(FileUtil.readFile(arrayOfFile[j])));
    }
  }

  public PageList<User> saveAndGetFriends(String paramString)
    throws Exception
  {
    JSONObject localJSONObject1 = new JSONObject(paramString);
    JSONArray localJSONArray = localJSONObject1.getJSONArray("users");
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    int j = localJSONArray.length();
    while (true)
    {
      if (i >= j)
      {
        PageList localPageList = new PageList();
        localPageList.records = localArrayList;
        localPageList.totalCount = localJSONObject1.optInt("total_number");
        localPageList.nextStartIndex = localJSONObject1.optInt("next_cursor");
        localPageList.prevPage = localJSONObject1.optInt("previous_cursor");
        return localPageList;
      }
      JSONObject localJSONObject2 = localJSONArray.getJSONObject(i);
      User localUser = UserBuilder.fromResponse(localJSONObject2);
      localArrayList.add(localUser);
      String str = this.mFriendsDir.getAbsolutePath() + File.separator + localUser.id;
      FileUtil.createFile(str);
      FileUtil.writeFile(str, localJSONObject2.toString().getBytes(), false);
      i++;
    }
  }
}