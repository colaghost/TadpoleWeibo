
package org.tadpoleweibo.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class JSONUtil {
    /**
     * 根据相同的属性名，从jsonobj拷贝到obj。
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
                    value = Float.valueOf((float)jsonObj.optDouble(fieldName));
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

    /**
     * 根据obj的属性名，创建json对象。
     * 
     * @param obj
     * @param clazz
     * @param jsonObj
     */
    public static JSONObject object2Json(Object obj, Class clazz) {
        try {
            JSONObject jsonObject = new JSONObject();
            String fieldName = "";
            for (Field field : clazz.getFields()) {
                fieldName = field.getName();
                field.setAccessible(true);
                if (int.class == field.getType() || field.getType() == Integer.class) {
                    jsonObject.put(fieldName, field.getInt(obj));
                } else if (float.class == field.getType() || field.getType() == Float.class) {
                    jsonObject.put(fieldName, field.getFloat(obj));

                } else if (long.class == field.getType() || field.getType() == Long.class) {
                    jsonObject.put(fieldName, field.getLong(obj));
                } else if (double.class == field.getType() || field.getType() == Double.class) {
                    jsonObject.put(fieldName, field.getDouble(obj));
                } else if (String.class == field.getType()) {
                    jsonObject.put(fieldName, (String)field.get(obj));
                } else {
                    continue;
                }
            }
            return jsonObject;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
