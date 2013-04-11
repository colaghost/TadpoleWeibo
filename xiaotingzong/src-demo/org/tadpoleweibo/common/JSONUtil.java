package org.tadpoleweibo.common;

import java.lang.reflect.Field;

import org.json.JSONObject;

public class JSONUtil {
    public static void copyProperties(Object obj, Class clazz, JSONObject jsonObj) {
        try {
            Object value = null;
            String fieldName = "";
            Object jsonValue = null;
            for (Field field : clazz.getFields()) {
                fieldName = field.getName();
                if (!jsonObj.has(fieldName)) {
                    System.out.println("fileName = " + fieldName + " not exist");
                    continue;
                }
                field.setAccessible(true);
                if (int.class == field.getType() || field.getType() == Integer.class) {
                    value = Integer.valueOf(jsonObj.optInt(fieldName));
                }else
                if (float.class == field.getType() || field.getType() == Float.class) {
                    value = Float.valueOf((float) jsonObj.optDouble(fieldName));
                }else
                if (long.class == field.getType() || field.getType() == Long.class) {
                    value = Long.valueOf(jsonObj.optLong(fieldName));
                }else
                if (double.class == field.getType() || field.getType() == Double.class) {
                    value = Double.valueOf(jsonObj.optDouble(fieldName));
                }else
                if (String.class == field.getType()) {
                    value = String.valueOf(jsonObj.optString(fieldName));
                }else{
                    System.out.println("fileName = " + fieldName + " 不是基本类型");
                    return;
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