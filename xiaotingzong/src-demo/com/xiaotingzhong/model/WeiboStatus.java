
package com.xiaotingzhong.model;

import com.xiaotingzhong.app.XTZApplication;
import com.xiaotingzhong.widget.WeiboStatusesListAdapter.ShowUserAsyncTask;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Log.d(TAG, "fromResponse = " + jsonObject.toString());
        Log.d(TAG, "fromResponse = " + jsonObject.has("user"));

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
            item.mTextSpanString = getTextSpannableString(XTZApplication.app, "@"
                    + item.user.screen_name + ":" + item.text);
        } else {
            item.mTextSpanString = getTextSpannableString(XTZApplication.app, item.text);
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
        /*
         * @[^\\s:：]+[:：\\s] 匹配@某人 \\[[^0-9]{1,4}\\] 匹配表情 #([^\\#|.]+)# 匹配#某主题
         * http://t\\.cn/\\w+ 匹配网址
         */
        Pattern pattern = Pattern
                .compile("@[\\u4e00-\\u9fa5\\w\\-]+|#([^\\#|.]+)#|http://t\\.cn/\\w+|\\[[^0-9]{1,4}\\]");
        Matcher matcher = pattern.matcher(spannableString);
        final Context mcontext = context;
        while (matcher.find()) {
            final String match = matcher.group();
            if (match.startsWith("@")) { // @某人，加亮字体
                spannableString.setSpan(new ClickableSpan() {
                    // 在onClick方法中可以编写单击链接时要执行的动作
                    @Override
                    public void onClick(View v) {
                        String screen_name = match;
                        screen_name = screen_name.replace("@", "");
                        screen_name = screen_name.replace(":", "");
                        screen_name = screen_name.trim();
                        Log.d("getTextSpannableString", "screen_name = " + screen_name);
                        new ShowUserAsyncTask(v.getContext(), XTZApplication.getCurUser(), 0,
                                screen_name).execute("");
                    }
                }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(0xff0077ff), matcher.start(),
                        matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (match.startsWith("#")) { // #某主题
                spannableString.setSpan(new ClickableSpan() {
                    // 在onClick方法中可以编写单击链接时要执行的动作
                    @Override
                    public void onClick(View widget) {
                        String theme = match;
                        theme = theme.replace("#", "");
                        // TODO 话题這個版本不做
                    }
                }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(0xff0077ff), matcher.start(),
                        matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (match.startsWith("http://")) { // 匹配网址
                spannableString.setSpan(new ClickableSpan() {
                    // 在onClick方法中可以编写单击链接时要执行的动作
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(match);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        v.getContext().startActivity(intent);
                    }
                }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(0xff0077ff), matcher.start(),
                        matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                            emotion.icon);
                    Log.d(TAG, "phrase = " + phrase + ", url = " + emotion.icon + ",drawable = "
                            + drawable);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, pixel, pixel);
                        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                        spannableString.setSpan(imageSpan, matcher.start(), matcher.end(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return spannableString;
    }

}
