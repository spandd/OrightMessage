package com.example.a33626.endhomework2.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个就是那个工具类
 * 具体细节就不说了
 * 每个实体类都 实现了序列化 接口  保证了传输的可靠性 不会有问题
 */
public class JsonUtil {

    /**
     *  json字符串转javabean/
     */

    public static Object jsonToObject(String jsonStr,Class c){
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Object returnObject = c.newInstance(); //获取要转到的对象
            Field[] fields = c.getDeclaredFields(); //获取其属性
            for (int i = 0;i < fields.length;i++) {
                fields[i].setAccessible(true);
                if (jsonObject.isNull(fields[i].getName())){
                    continue;
                }
                else {
                    fields[i].set(returnObject,jsonObject.get(fields[i].getName()));
                }

            }
            return returnObject;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  javabean对象转Jsond对象
     */
    /**
     * 其实这里面的原理很简单 就是把 实体类里所有的 私有字段 全都 允许访问 然后把json里的 值全都取出来 然后放到 实例化后的对象里
     *
     * 转换的话 反之同理
     */
    //----------------------------------------------这里的这个第二个参数 就是 要转成的对象的类
    public static JSONObject objectToJson(Object obj,Class c){
        try {
            JSONObject jsonObject = new JSONObject();
            Field[] fields = c.getDeclaredFields(); //获取其属性
            for (int i = 0;i < fields.length;i++) {
                fields[i].setAccessible(true);
                Object fieldValue = fields[i].get(obj);
                if (fieldValue == null){
                    continue;
                }
                //这些就是 把值都注入到字段里
                jsonObject.put(fields[i].getName(),fieldValue);
            }
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json数组转JavaBeanList
     */

    public static List jsonArrayToObjectList(String jsonStr, Class c){
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            List returnObjectList = new ArrayList<>();
            for (int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Object returnObject = c.newInstance(); //获取要转到的对象
                Field[] fields = c.getDeclaredFields(); //获取其属性
                for (int j = 0;j < fields.length;j++) {
                    fields[j].setAccessible(true);
                    if (jsonObject.isNull(fields[j].getName())){
                        continue;
                    }
                    else {
                        fields[j].set(returnObject,jsonObject.get(fields[j].getName()));
                    }
                }
                returnObjectList.add(returnObject);
            }
            return returnObjectList;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
