package com.weibo.sdk.android.api.response;

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
        ArrayList<Emotion> list = loadEmotionsFromRemote(EMOTION_TYPE.FACE, LANGUAGE.cnname);
        for (Emotion e : list) {
            map.put(e.phrase, e);
        }

        list = loadEmotionsFromRemote(EMOTION_TYPE.FACE, LANGUAGE.twname);
        for (Emotion e : list) {
            map.put(e.phrase, e);
        }
    }


    /**
     * 加载表情到本地
     * 
     * @return
     * @throws Exception
     */
    public static ArrayList<Emotion> loadEmotionsFromRemote(EMOTION_TYPE type, LANGUAGE language) throws Exception {
        ArrayList<Emotion> list = new ArrayList<Emotion>();
        String resp = XTZApplication.getStatusesAPI().emotions(type, language);
        JSONArray jsonArr = new JSONArray(resp);
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
