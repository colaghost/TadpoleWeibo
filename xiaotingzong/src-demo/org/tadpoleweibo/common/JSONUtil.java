package org.tadpoleweibo.common;

import java.lang.reflect.Field;

import org.json.JSONObject;

public class JSONUtil {
    /**
     * 对obj，jsonobj相同的属性名的值进行赋值拷贝。
     * 
     * @param obj
     * @param clazz
     * @param jsonObj
     */
    public static void copyProperties(Object obj, Class clazz, JSONObject jsonObj) {
        try {
            Object value = null;
            String fieldName = "";
            for (Field field : clazz.getFields()) {
                fieldName = field.getName();
                if (!jsonObj.has(fieldName)) {
                    continue;
                }
                field.setAccessible(true);
                if (int.class == field.getType() || field.getType() == Integer.class) {
                    value = Integer.valueOf(jsonObj.optInt(fieldName));
                } else if (float.class == field.getType() || field.getType() == Float.class) {
                    value = Float.valueOf((float) jsonObj.optDouble(fieldName));
                } else if (long.class == field.getType() || field.getType() == Long.class) {
                    value = Long.valueOf(jsonObj.optLong(fieldName));
                } else if (double.class == field.getType() || field.getType() == Double.class) {
                    value = Double.valueOf(jsonObj.optDouble(fieldName));
                } else if (String.class == field.getType()) {
                    value = String.valueOf(jsonObj.optString(fieldName));
                } else {
                    continue;
                }
                field.set(obj, value);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}