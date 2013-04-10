package com.weibo.sdk.android.api.response;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {

    private static final long serialVersionUID = 7431294131953990833L;

    public int id;
    public String screen_name;
    public String name;
    public String profile_image_url;
    public String avatar_large;
}
