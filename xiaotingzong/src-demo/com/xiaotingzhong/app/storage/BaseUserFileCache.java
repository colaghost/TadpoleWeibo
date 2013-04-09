package com.xiaotingzhong.app.storage;

import android.content.Context;
import java.io.File;
import org.tadpoleweibo.common.FileUtil;

public class BaseUserFileCache {
    private String mCachePath;

    public BaseUserFileCache(Context context, int paramInt) {
        this.mCachePath = (context.getFilesDir() + File.separator + paramInt + File.separator);
        FileUtil.createDir(this.mCachePath);
    }

    public File getSubDir(String name) {
        String str = this.mCachePath + name + File.separator;
        FileUtil.createDir(str);
        return new File(str);
    }

    public File getSubFile(String name) {
        String str = this.mCachePath + name;
        FileUtil.createFile(str);
        return new File(str);
    }
}