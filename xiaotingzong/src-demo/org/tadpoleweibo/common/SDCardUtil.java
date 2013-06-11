
package org.tadpoleweibo.common;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;

public class SDCardUtil {
    /**
     * 获取
     * @return
     * @throws FileNotFoundException
     */
    public static String getSDPath() throws FileNotFoundException {
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

}
