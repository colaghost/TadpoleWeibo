
package org.tadpoleweibo.widget.image;

import org.tadpoleweibo.common.FileUtil;
import org.tadpoleweibo.common.SDCardUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class ImageDiskCache {

    private boolean mImageDiskCacheEnable = true;

    private String mDirPath;

    public ImageDiskCache(String dirPath) {
        mDirPath = dirPath;
        try {
            if (mDirPath == null) {
                mDirPath = SDCardUtil.getSDPath() + "/xxxx/";
            }
            File file = new File(mDirPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mImageDiskCacheEnable = false;
        }
    }

    public String getImageCacheFilePath(String key) {
        return mDirPath + File.separator + key;
    }

    public boolean hasCache(String key) {
        if (!mImageDiskCacheEnable) {
            return false;
        }
        return FileUtil.exists(getImageCacheFilePath(key));
    }

    public void writeToDisk(String key, byte[] bitmapBytes) {
        if (!mImageDiskCacheEnable) {
            return;
        }
        System.out.println("mDirPath = " + getImageCacheFilePath(key));

        boolean succ = FileUtil.createFile(mDirPath + key);
        if (succ) {
            FileUtil.writeFile(getImageCacheFilePath(key), bitmapBytes, false);
        }
    }

    public byte[] readFromDisk(String key) throws IOException {
        return FileUtil.readFile(getImageCacheFilePath(key));
    }

    public void deleteFromDisk(String key) {
        FileUtil.delete(getImageCacheFilePath(key));
    }

}
