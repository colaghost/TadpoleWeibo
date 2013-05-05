
package org.tadpoleweibo.widget.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.tadpoleweibo.common.FileUtil;

import android.os.Environment;

public class ImageDiskCache {

    private String mDirPath;

    public ImageDiskCache(String dirPath) {
        mDirPath = dirPath;
        if (mDirPath == null) {
            try {
                mDirPath = getSDPath() + "/xxxx/";
                File file = new File(mDirPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasCache(String key) {
        return FileUtil.exists(mDirPath + key);
    }

    public void writeToDisk(String key, byte[] bitmapBytes) {

        System.out.println("mDirPath = " + mDirPath + key);

        boolean succ = FileUtil.createFile(mDirPath + key);
        if (succ) {
            FileUtil.writeFile(mDirPath + key, bitmapBytes, false);
        }
    }

    public byte[] readFromDisk(String key) throws IOException {
        return FileUtil.readFile(mDirPath + key);
    }

    public String getSDPath() throws FileNotFoundException {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }

        if (sdDir == null) {
            throw new FileNotFoundException("not sd card found");
        }

        return sdDir.toString();

    }

    public void deleteFromDish(String key) {
        FileUtil.delete(mDirPath + key);
    }

}
