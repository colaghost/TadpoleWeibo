package com.xiaotingzhong.model.cache;

import java.io.File;
import java.util.ArrayList;

import org.tadpoleweibo.common.FileUtil;

import android.content.Context;

import com.weibo.sdk.android.api.WeiboAPI.EMOTION_TYPE;
import com.weibo.sdk.android.api.WeiboAPI.LANGUAGE;
import com.xiaotingzhong.model.Emotion;

public class EmotionCache {
	private String mCachePath;
	private String mCacheKey;
	private File mCacheFile;

	public EmotionCache(Context context, EMOTION_TYPE e, LANGUAGE l) {
		this.mCachePath = (context.getFilesDir() + File.separator + "emotions");
		FileUtil.createDir(this.mCachePath);
		mCacheKey = "" + e.ordinal() + l.ordinal();
		mCacheFile = new File(mCachePath + File.separator + mCacheKey);
	}

	public boolean exists() {
		return mCacheFile.exists();
	}

	public ArrayList<Emotion> getEmotions() throws Exception {
		String response = FileUtil.readFile(mCacheFile);
		return Emotion.fromEmotionsResponse(response);
	}

	public void saveEmotionsResponse(String response) {
		FileUtil.createFile(mCacheFile.getAbsolutePath());
		FileUtil.writeFile(mCacheFile, response.getBytes(), false);
	}

}
