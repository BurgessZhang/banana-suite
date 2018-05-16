package com.burgess.banana.common.json;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.json
 * @file BananaJsonUtils.java
 * @time 2018-05-16 16:18
 * @desc json工具类
 */
public class BananaJsonUtils {

    private static BananaJsonMapper jsonMapper = BananaJsonMapper.getDefault();

    public static ObjectMapper getMapper(){
        return jsonMapper.getMapper();
    }

    public static String toJson(Object object) {
        return jsonMapper.toJson(object);
    }
    /**
     * 不含值为null的属性
     * @param object
     * @return
     */
    public static String toJsonIgnoreNullField(Object object) {
        return BananaJsonMapper.nonNullMapper().toJson(object);
    }

    public static <T> T toObject(String jsonString, Class<T> clazz) {
        return  jsonMapper.toObject(jsonString, clazz);
    }

    public static <T> List<T> toList(String jsonString, Class<T> clazz) {
        return jsonMapper.toList(jsonString, clazz);
    }

    public static JsonNode getNode(String jsonString,String nodeName){
        try {
            JsonNode node = jsonMapper.getMapper().readTree(jsonString);
            return nodeName == null ? node : node.get(nodeName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param jsonString
     * @param attrs (e.g:info.user.id)
     * @return
     */
    public static String getJsonNodeValue(String jsonString, String attrs) {
        return getJsonNodeValue(getNode(jsonString, null), attrs);
    }

    /**
     *
     * @param node
     * @param attrs (e.g:info.user.id)
     * @return
     */
    public static String getJsonNodeValue(JsonNode node, String attrs) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (node != null && node.get(attrs) != null) {
                return node.get(attrs).asText();
            }
            return null;
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            return getJsonNodeValue(node.get(s1), s2);
        }
    }

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        list.add(new Date());
        System.out.println(toJson(list));
    }
}
