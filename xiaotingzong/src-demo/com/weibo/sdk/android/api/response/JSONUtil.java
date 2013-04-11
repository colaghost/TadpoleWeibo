package com.weibo.sdk.android.api.response;

import java.lang.reflect.Field;

import org.json.JSONObject;

import android.util.Property;

public class JSONUtil {
    public static void copyProperties(Object obj, Class clazz, JSONObject jsonObj) {
        for (Field field : clazz.getFields()) {
            String fieldName = field.getName() ;
            jsonObj.opt(fieldName);
            
            
//            field.getType()
            
        }
    }
}
