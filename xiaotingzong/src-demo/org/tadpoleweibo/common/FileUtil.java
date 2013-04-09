package org.tadpoleweibo.common;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.util.Log;

public class FileUtil
{
  public static final int BUFFER_SIZE = 8192;
  public static final String CLASS_NAME = "FileUtil";

  static void close(Closeable paramCloseable)
  {
    if (paramCloseable != null);
    try
    {
      paramCloseable.close();
      return;
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
  }

  public static File copyFile(InputStream paramInputStream, String paramString, boolean paramBoolean)
    throws IOException
  {
    File localFile;
    if ((paramInputStream == null) || (paramString == null))
      localFile = null;
    while (true)
    {
      return localFile;
      localFile = new File(paramString);
      if ((!paramBoolean) && (localFile.exists()))
      {
        localFile = null;
        continue;
      }
      if ((localFile.exists()) || (localFile.createNewFile()))
        break;
      localFile = null;
    }
    byte[] arrayOfByte = new byte[8192];
    FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
    while (true)
    {
      int i = paramInputStream.read(arrayOfByte);
      if (i <= 0)
      {
        paramInputStream.close();
        localFileOutputStream.close();
        break;
      }
      localFileOutputStream.write(arrayOfByte, 0, i);
    }
  }

  public static boolean copyFile(String paramString1, String paramString2, boolean paramBoolean)
  {
    int i = 0;
    if ((paramString1 == null) || (paramString2 == null));
    while (true)
    {
      return i;
      if (!new File(paramString1).exists())
        continue;
      try
      {
        File localFile = new File(paramString2);
        if (((!paramBoolean) && (localFile.exists())) || ((!localFile.exists()) && (!localFile.createNewFile())))
          continue;
        FileInputStream localFileInputStream = new FileInputStream(localFile);
        FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
        byte[] arrayOfByte = new byte[8192];
        while (true)
        {
          int j = localFileInputStream.read(arrayOfByte);
          if (j <= 0)
          {
            localFileInputStream.close();
            localFileOutputStream.close();
            i = 1;
            break;
          }
          localFileOutputStream.write(arrayOfByte, 0, j);
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public static boolean copyFile(byte[] paramArrayOfByte, String paramString, boolean paramBoolean)
  {
    int i = 0;
    if ((paramArrayOfByte == null) || (paramString == null));
    while (true)
    {
      return i;
      try
      {
        File localFile = new File(paramString);
        if (((!paramBoolean) && (localFile.exists())) || ((!localFile.exists()) && (!localFile.createNewFile())))
          continue;
        FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
        localFileOutputStream.write(paramArrayOfByte);
        localFileOutputStream.close();
        i = 1;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public static boolean createDir(String paramString)
  {
    int i = 0;
    File localFile = new File(paramString);
    if (localFile.exists())
      if (!localFile.isDirectory());
    while (true)
    {
      return i;
      localFile.delete();
      try
      {
        boolean bool = localFile.mkdirs();
        i = bool;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public static boolean createFile(String paramString)
  {
    int i = 0;
    File localFile = new File(paramString);
    if (localFile.exists());
    while (true)
    {
      return i;
      try
      {
        boolean bool = localFile.createNewFile();
        i = bool;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public static boolean createFile(String paramString1, String paramString2)
  {
    int i = 0;
    File localFile = new File(paramString2);
    if (localFile.exists());
    while (true)
    {
      return i;
      try
      {
        if (!createDir(paramString1))
          continue;
        boolean bool = localFile.createNewFile();
        i = bool;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public static void delete(String paramString)
  {
    if (paramString == null);
    File localFile;
    do
    {
      return;
      localFile = new File(paramString);
    }
    while ((localFile == null) || (!localFile.exists()));
    File[] arrayOfFile;
    if (localFile.isDirectory())
      arrayOfFile = localFile.listFiles();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfFile.length)
      {
        localFile.delete();
        break;
      }
      delete(arrayOfFile[i].toString());
    }
  }

  public static void deleteSubFile(String paramString)
  {
    if (paramString == null);
    while (true)
    {
      return;
      File localFile = new File(paramString);
      if ((localFile == null) || (!localFile.exists()) || (!localFile.isDirectory()))
        continue;
      File[] arrayOfFile = localFile.listFiles();
      for (int i = 0; i < arrayOfFile.length; i++)
        delete(arrayOfFile[i].toString());
    }
  }

  public static boolean exists(String paramString)
  {
    return new File(paramString).exists();
  }

  public static long getDirSize(File paramFile)
  {
    long l = 0L;
    if (paramFile == null);
    do
      return l;
    while (!paramFile.isDirectory());
    l = 0L;
    File[] arrayOfFile = paramFile.listFiles();
    int i = arrayOfFile.length;
    int j = 0;
    label29: File localFile;
    if (j < i)
    {
      localFile = arrayOfFile[j];
      if (!localFile.isFile())
        break label64;
      l += localFile.length();
    }
    while (true)
    {
      j++;
      break label29;
      break;
      label64: if (!localFile.isDirectory())
        continue;
      l = l + localFile.length() + getDirSize(localFile);
    }
  }

  public static long getDirSizeByPath(String paramString)
  {
    File localFile = new File(paramString);
    long l;
    if (localFile.exists())
      l = getDirSize(localFile);
    while (true)
    {
      return l;
      l = 0L;
    }
  }

  public static long getLastModified(String paramString)
  {
    File localFile = new File(paramString);
    long l;
    if (localFile.exists())
      l = localFile.lastModified();
    while (true)
    {
      return l;
      l = 0L;
    }
  }

  public static String readFile(File paramFile)
    throws IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(paramFile);
    byte[] arrayOfByte = new byte[localFileInputStream.available()];
    localFileInputStream.read(arrayOfByte);
    localFileInputStream.close();
    return new String(arrayOfByte);
  }

  public static void rename(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null));
    while (true)
    {
      return;
      File localFile = new File(paramString1);
      if (!localFile.exists())
        continue;
      localFile.renameTo(new File(paramString2));
    }
  }

  public static void unZip(Activity paramActivity, String paramString1, String paramString2)
  {
    File localFile = new File(paramString1);
    if ((paramActivity == null) || (!localFile.exists()));
    while (true)
    {
      return;
      try
      {
        unZip(new FileInputStream(paramString1), paramString2);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public static boolean unZip(InputStream paramInputStream, String paramString)
  {
    int i = 1;
    try
    {
      File localFile1 = new File(paramString);
      if (!localFile1.exists())
        localFile1.mkdirs();
      ZipInputStream localZipInputStream = new ZipInputStream(paramInputStream);
      ZipEntry localZipEntry = localZipInputStream.getNextEntry();
      if (localZipEntry == null)
      {
        localZipInputStream.close();
      }
      else
      {
        if (localZipEntry.isDirectory())
        {
          String str1 = localZipEntry.getName();
          String str2 = ((String)str1).substring(0, -1 + ((String)str1).length());
          new File(paramString + File.separator + (String)str2).mkdirs();
        }
        while (true)
        {
          localZipEntry = localZipInputStream.getNextEntry();
          break;
          File localFile2 = new File(paramString + File.separator + localZipEntry.getName());
          if (localFile2.exists())
            localFile2.delete();
          localFileOutputStream = new FileOutputStream(localFile2);
          arrayOfByte = new byte[8192];
          j = localZipInputStream.read(arrayOfByte);
          if (j > 0)
            break label255;
          localFileOutputStream.flush();
          localFileOutputStream.getFD().sync();
          localFileOutputStream.close();
        }
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        FileOutputStream localFileOutputStream;
        byte[] arrayOfByte;
        int j;
        localException.printStackTrace();
        i = 0;
        break;
        label255: localFileOutputStream.write(arrayOfByte, 0, j);
      }
    }
    return i;
  }

  // ERROR //
  public static boolean unZip(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 62	java/io/FileInputStream
    //   7: dup
    //   8: aload_0
    //   9: invokespecial 132	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   12: astore 4
    //   14: aload 4
    //   16: aload_1
    //   17: invokestatic 135	org/tadpoleweibo/common/FileUtil:unZip	(Ljava/io/InputStream;Ljava/lang/String;)Z
    //   20: istore 9
    //   22: iload 9
    //   24: istore_2
    //   25: aload 4
    //   27: ifnull +68 -> 95
    //   30: aload 4
    //   32: invokevirtual 65	java/io/FileInputStream:close	()V
    //   35: iload_2
    //   36: ireturn
    //   37: astore 5
    //   39: aload 5
    //   41: invokevirtual 188	java/io/FileNotFoundException:printStackTrace	()V
    //   44: aload_3
    //   45: ifnull -10 -> 35
    //   48: aload_3
    //   49: invokevirtual 65	java/io/FileInputStream:close	()V
    //   52: goto -17 -> 35
    //   55: astore 8
    //   57: aload 8
    //   59: invokevirtual 26	java/io/IOException:printStackTrace	()V
    //   62: goto -27 -> 35
    //   65: astore 6
    //   67: aload_3
    //   68: ifnull +7 -> 75
    //   71: aload_3
    //   72: invokevirtual 65	java/io/FileInputStream:close	()V
    //   75: aload 6
    //   77: athrow
    //   78: astore 7
    //   80: aload 7
    //   82: invokevirtual 26	java/io/IOException:printStackTrace	()V
    //   85: goto -10 -> 75
    //   88: astore 10
    //   90: aload 10
    //   92: invokevirtual 26	java/io/IOException:printStackTrace	()V
    //   95: goto -60 -> 35
    //   98: astore 6
    //   100: aload 4
    //   102: astore_3
    //   103: goto -36 -> 67
    //   106: astore 5
    //   108: aload 4
    //   110: astore_3
    //   111: goto -72 -> 39
    //
    // Exception table:
    //   from	to	target	type
    //   4	14	37	java/io/FileNotFoundException
    //   48	52	55	java/io/IOException
    //   4	14	65	finally
    //   39	44	65	finally
    //   71	75	78	java/io/IOException
    //   30	35	88	java/io/IOException
    //   14	22	98	finally
    //   14	22	106	java/io/FileNotFoundException
  }

  public static boolean writeFile(File paramFile, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(paramFile, paramBoolean);
      localFileOutputStream.write(paramArrayOfByte);
      localFileOutputStream.close();
      i = 1;
      return i;
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        Log.e("FileUtil", localIOException.getMessage());
        int i = 0;
      }
    }
  }

  public static boolean writeFile(String paramString, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return writeFile(new File(paramString), paramArrayOfByte, paramBoolean);
  }

  // ERROR //
  public static boolean writeLine(File paramFile, String paramString, boolean paramBoolean)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: new 42	java/io/FileOutputStream
    //   5: dup
    //   6: aload_0
    //   7: iload_2
    //   8: invokespecial 193	java/io/FileOutputStream:<init>	(Ljava/io/File;Z)V
    //   11: astore 4
    //   13: aload 4
    //   15: new 159	java/lang/StringBuilder
    //   18: dup
    //   19: aload_1
    //   20: invokestatic 163	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   23: invokespecial 164	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   26: ldc 209
    //   28: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   31: invokevirtual 172	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   34: invokevirtual 213	java/lang/String:getBytes	()[B
    //   37: invokevirtual 70	java/io/FileOutputStream:write	([B)V
    //   40: aload 4
    //   42: invokestatic 215	org/tadpoleweibo/common/FileUtil:close	(Ljava/io/Closeable;)V
    //   45: iconst_1
    //   46: istore 8
    //   48: iload 8
    //   50: ireturn
    //   51: astore 5
    //   53: ldc 11
    //   55: aload 5
    //   57: invokevirtual 196	java/io/IOException:getMessage	()Ljava/lang/String;
    //   60: invokestatic 202	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   63: pop
    //   64: aload_3
    //   65: invokestatic 215	org/tadpoleweibo/common/FileUtil:close	(Ljava/io/Closeable;)V
    //   68: iconst_0
    //   69: istore 8
    //   71: goto -23 -> 48
    //   74: astore 6
    //   76: aload_3
    //   77: invokestatic 215	org/tadpoleweibo/common/FileUtil:close	(Ljava/io/Closeable;)V
    //   80: aload 6
    //   82: athrow
    //   83: astore 6
    //   85: aload 4
    //   87: astore_3
    //   88: goto -12 -> 76
    //   91: astore 5
    //   93: aload 4
    //   95: astore_3
    //   96: goto -43 -> 53
    //
    // Exception table:
    //   from	to	target	type
    //   2	13	51	java/io/IOException
    //   2	13	74	finally
    //   53	64	74	finally
    //   13	40	83	finally
    //   13	40	91	java/io/IOException
  }
}