package com.xiaotingzhong.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tadpoleweibo.common.JSONUtil;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.WeiboAPI.EMOTION_TYPE;
import com.weibo.sdk.android.api.WeiboAPI.LANGUAGE;
import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.model.cache.EmotionCache;

public class Emotion {
	public String category;
	public boolean common;
	public boolean hot;
	public String icon;
	public String phrase;
	public String picid;
	public String type;
	public String url;
	public String value;

	public static Emotion fromResponse(JSONObject jsonObj) {
		if (jsonObj == null) {
			return null;
		}
		Emotion e = new Emotion();
		JSONUtil.copyProperties(e, Emotion.class, jsonObj);
		return e;
	}

	public static final HashMap<String, Emotion> map = new HashMap<String, Emotion>();

	public static void cacheEmotions() throws Exception {
		cacheEmotions(EMOTION_TYPE.FACE, LANGUAGE.cnname);
		cacheEmotions(EMOTION_TYPE.FACE, LANGUAGE.twname);
	}

	private static void cacheEmotions(EMOTION_TYPE type, LANGUAGE language)
			throws Exception {
		ArrayList<Emotion> list = getEmotionsPreferCache(type, language);
		if (list != null) {
			for (Emotion e : list) {
				map.put(e.phrase, e);
			}
		}
	}

	public static ArrayList<Emotion> getEmotionsPreferCache(EMOTION_TYPE type,
			LANGUAGE language) throws Exception {
		EmotionCache ec = new EmotionCache(XTZApplication.app, type, language);
		ArrayList<Emotion> list = null;
		if (ec.exists()) {
			list = ec.getEmotions();
		} else {
			String resp = XTZApplication.getStatusesAPI().emotions(type,
					language);
			list = fromEmotionsResponse(resp);
			ec.saveEmotionsResponse(resp);
		}
		return list;
	}

	/**
	 * 获取表情到本地
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Emotion> getEmotionsFromRemote(EMOTION_TYPE type,
			LANGUAGE language) throws Exception {
		String resp = XTZApplication.getStatusesAPI().emotions(type, language);
		return fromEmotionsResponse(resp);
	}

	public static ArrayList<Emotion> fromEmotionsResponse(String response)
			throws Exception {
		ArrayList<Emotion> list = new ArrayList<Emotion>();
		JSONArray jsonArr = new JSONArray(response);
		Emotion eTmp = null;
		for (int i = 0, len = jsonArr.length(); i < len; i++) {
			eTmp = fromResponse(jsonArr.optJSONObject(i));
			if (eTmp != null) {
				list.add(eTmp);
			}
		}
		return list;
	}
}
