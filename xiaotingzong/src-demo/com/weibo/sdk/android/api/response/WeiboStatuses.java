package com.weibo.sdk.android.api.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tadpoleweibo.common.JSONUtil;
import org.tadpoleweibo.widget.PageList;
import org.w3c.dom.ls.LSInput;

/**
 * https://api.weibo.com/2/statuses
 */
public class WeiboStatuses {
    public long id;
    public String created_at;
    public String text;
    public String source;
    public int reposts_count;
    public int comments_count;
    public String in_reply_to_status_id;
    public String in_reply_to_user_id;
    public String in_reply_to_screen_name;
    public String thumbnail_pic;
    public String bmiddle_pic;
    public String original_pic;


    public static WeiboStatuses fromResponse(JSONObject jsonObject) {
        WeiboStatuses item = new WeiboStatuses();
        JSONUtil.copyProperties(item, WeiboStatuses.class, jsonObject);
        return item;
    }


    public static WeiboStatuses fromResponse(String response) {
        try {
            return fromResponse(new JSONObject(response));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PageList<WeiboStatuses> fromUserTimelineJson(String response) throws JSONException {
        JSONObject jsonObj = new JSONObject(response);
        JSONArray statusesJsonArr = jsonObj.optJSONArray("statuses");
        WeiboStatuses statuses = null;
        JSONObject jo = null;

        ArrayList<WeiboStatuses> list = new ArrayList<WeiboStatuses>();
        for (int i = 0, len = statusesJsonArr.length(); i < len; i++) {
            jo = statusesJsonArr.getJSONObject(i);
            statuses = WeiboStatuses.fromResponse(jo);
            list.add(statuses);
        }

        PageList<WeiboStatuses> pageList = new PageList<WeiboStatuses>();
        pageList.records = list;
        pageList.total_number = jsonObj.optInt("total_number");
        pageList.previous_cursor = jsonObj.optInt("previous_cursor");
        pageList.next_cursor = jsonObj.optInt("next_cursor");

        return pageList;
    }

}
