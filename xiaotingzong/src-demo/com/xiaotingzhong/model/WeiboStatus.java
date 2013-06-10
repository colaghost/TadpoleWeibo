
package com.xiaotingzhong.model;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.tadpole.R;
import org.tadpoleweibo.common.JSONUtil;
import org.tadpoleweibo.common.StringUtil;
import org.tadpoleweibo.widget.image.ImageHelper;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.widget.WeiboStatusesListAdapter.ShowUserAsyncTask;
import com.xiaotingzhong.widget.span.StatusAtClickableSpan;

/**
 * https://api.weibo.com/2/statuses
 */
public class WeiboStatus {
    private static final String TAG = "WeiboStatuses";

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

    public WeiboStatus retweeted_status;

    public User user;

    //
    protected CharSequence mTextSpanString;

    public long getCreateAtLong() {
        return new Date(created_at).getTime();
    }

    public String getCreateAt() {
        if (!StringUtil.isEmpty(created_at)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return getInterval(format.format(new Date(created_at)));
        }
        return "";
    }

    public String getInterval(String createtime) { // 传入的时间格式必须类似于2012-8-21
                                                   // 17:53:20这样的格式
        String interval = null;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date d1 = (Date)sd.parse(createtime, pos);

        // 用现在距离1970年的时间间隔new
        // Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
        long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

        if (time / 1000 < 10 && time / 1000 >= 0) {
            // 如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
            interval = "刚刚";

        } else if (time / 3600000 < 24 && time / 3600000 >= 0) {
            // 如果时间间隔小于24小时则显示多少小时前
            int h = (int)(time / 3600000);// 得出的时间间隔的单位是小时
            interval = h + "小时前";

        } else if (time / 60000 < 60 && time / 60000 > 0) {
            // 如果时间间隔小于60分钟则显示多少分钟前
            int m = (int)((time % 3600000) / 60000);// 得出的时间间隔的单位是分钟
            interval = m + "分钟前";

        } else if (time / 1000 < 60 && time / 1000 > 0) {
            // 如果时间间隔小于60秒则显示多少秒前
            int se = (int)((time % 60000) / 1000);
            interval = se + "秒前";

        } else {
            // 大于24小时，则显示正常的时间，但是不显示秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            ParsePosition pos2 = new ParsePosition(0);
            Date d2 = (Date)sdf.parse(createtime, pos2);

            interval = sdf.format(d2);
        }
        return interval;
    }

    public CharSequence getTextSpannaleString() {
        return mTextSpanString;
    }

    public static WeiboStatus fromResponse(JSONObject jsonObject) {
        return fromResponse(jsonObject, false);
    }

    public static WeiboStatus fromResponse(JSONObject jsonObject, boolean isRetweet) {
        if (jsonObject == null) {
            return null;
        }
        WeiboStatus item = new WeiboStatus();
        if (jsonObject.has("user")) {
            JSONObject jo = jsonObject.optJSONObject("user");
            try {
                item.user = User.fromResponse(jo);
            } catch (Exception e) {
                e.printStackTrace();
                item.user = null;
            }
        }

        JSONUtil.copyProperties(item, WeiboStatus.class, jsonObject);
        if (isRetweet) {
            if (item.user == null) {
                return null;
            }
            item.mTextSpanString = getTextSpannableString(XTZApplication.sApp, "@"
                    + item.user.screen_name + ":" + item.text);
        } else {
            item.mTextSpanString = getTextSpannableString(XTZApplication.sApp, item.text);
        }

        if (jsonObject.has("retweeted_status")) {
            JSONObject jo = jsonObject.optJSONObject("retweeted_status");
            item.retweeted_status = fromResponse(jo, true);
        }

        return item;
    }

    public static WeiboStatus fromResponse(String response) {
        try {
            return fromResponse(new JSONObject(response));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将text中@某人、#某主题、http://网址的字体加亮，匹配的表情文字以表情显示
     * 
     * @param text
     * @param context
     * @return
     * @author lvqiyong
     */
    public static CharSequence getTextSpannableString(final Context context, String text) {
        if (StringUtil.isEmpty(text)) {
            return "";
        }
        SpannableString spannableString = new SpannableString(text);

        // @[^\\s:：]+[:：\\s] 匹配@某人
        // \\[[^0-9]{1,4}\\] 匹配表情
        // #([^\\#|.]+)# 匹配#某主题
        // http://t\\.cn/\\w+ 匹配网址
        Pattern pattern = Pattern
                .compile("@[\\u4e00-\\u9fa5\\w\\-]+|#([^\\#|.]+)#|http://t\\.cn/\\w+|\\[[^0-9]{1,4}\\]");
        Matcher matcher = pattern.matcher(spannableString);
        final Context mcontext = context;
        while (matcher.find()) {
            final String match = matcher.group();

            int start = matcher.start();
            int end = matcher.end();

            if (match.startsWith("@")) { // @某人，加亮字体
                spannableString.setSpan(new StatusAtClickableSpan(match), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(0xff0077ff), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // #某主题
            else if (match.startsWith("#")) {
                spannableString.setSpan(new ClickableSpan() {
                    // 在onClick方法中可以编写单击链接时要执行的动作
                    @Override
                    public void onClick(View widget) {
                        String theme = match;
                        theme = theme.replace("#", "");
                        // TODO 话题這個版本不做
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(0xff0077ff), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // 匹配网址
            else if (match.startsWith("http://")) {
                spannableString.setSpan(new ClickableSpan() {
                    // 在onClick方法中可以编写单击链接时要执行的动作
                    @Override
                    public void onClick(View v) {
                       
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(0xff0077ff), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (match.startsWith("[")) { // 表情

                int[] attrs = {
                    android.R.attr.textSize
                };
                TypedArray attr = context.obtainStyledAttributes(R.style.TextAppearance_Medium,
                        attrs);

                int pixel = attr.getDimensionPixelSize(0, 50) + 12;

                String phrase = match;
                Emotion emotion = XTZApplication.getEmotionByPhrase(phrase);
                if (emotion != null) {
                    BitmapDrawable drawable = ImageHelper.getBitmapByUrl(context.getResources(),
                            emotion.icon, null);
                    Log.d(TAG, "phrase = " + phrase + ", url = " + emotion.icon + ",drawable = "
                            + drawable);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, pixel, pixel);
                        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                        spannableString.setSpan(imageSpan, start, end,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return spannableString;
    }

}
