package com.weibo.sdk.android.api.response;

import java.util.ArrayList;

import org.tadpoleweibo.widget.PageList;

/**
 * https://api.weibo.com/2/statuses
 */
public class WeiboStatuses {
    public long id;
    public String created_at;
    public int text;
    public String source;
    public int reposts_count;
    public int comments_count;
    public String in_reply_to_status_id;
    public String in_reply_to_user_id;
    public String in_reply_to_screen_name;
    
    
    public static WeiboStatuses fromResponse(String response){
        WeiboStatuses item = new WeiboStatuses();
        
    }
    
    public static PageList<WeiboStatuses> fromUserTimelineJson(String response){
        ArrayList<WeiboStatuses> list = 
    }
}
