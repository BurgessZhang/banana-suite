package com.burgess.banana.common.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.json
 * @file BananaJsonMapper.java
 * @time 2018-05-16 16:08
 * @desc
 */
public class BananaJsonMapper {

    private static BananaJsonMapper defaultMapper;

    private ObjectMapper mapper;

    public BananaJsonMapper() {
        this(null);
    }

    public BananaJsonMapper(Include include) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);
    }

    public BananaJsonMapper enumAndStringConvert(boolean enabled) {
        if (enabled) {
            mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        } else {
            mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            mapper.disable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        }
        return this;

    }

    public BananaJsonMapper dateAndTimestampConvert(boolean enabled) {
        if (enabled) {
            mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.enable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        } else {
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
        return this;

    }

    /**
     * @param '[]
     * @return com.burgess.banana.common.json.BananaJsonMapper
     * @class_name BananaJsonMapper
     * @method nonEmptyMapper
     * @desc 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper, 建议在外部接口中使用.
     * @author free.zhang
     * @date 2018/5/16 16:16
     */
    public static BananaJsonMapper nonEmptyMapper() {

        return new BananaJsonMapper(Include.NON_EMPTY);
    }

    public static BananaJsonMapper nonNullMapper() {
        return new BananaJsonMapper(Include.NON_NULL);
    }

    /**
     * @param '[]
     * @return com.burgess.banana.common.json.BananaJsonMapper
     * @class_name BananaJsonMapper
     * @method nonDefaultMapper
     * @desc 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
     * @author free.zhang
     * @date 2018/5/16 16:15
     */
    public static BananaJsonMapper nonDefaultMapper() {

        return new BananaJsonMapper(Include.NON_DEFAULT);
    }

    /**
     * @param '[object]
     * @return java.lang.String
     * @class_name BananaJsonMapper
     * @method toJson
     * @desc Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     * @author free.zhang
     * @date 2018/5/16 16:15
     */
    public String toJson(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param '[jsonString, clazz]
     * @return T
     * @class_name BananaJsonMapper
     * @method toObject
     * @desc 反序列化POJO或简单Collection如List<String>.
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     * @author free.zhang
     * @date 2018/5/16 16:15
     */
    public <T> T toObject(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> toList(String jsonString, Class<T> elementType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, ArrayList.class, elementType);
        return toObject(jsonString, javaType);
    }

    /**
     * @param '[jsonString, javaType]
     * @return T
     * @class_name BananaJsonMapper
     * @method toObject
     * @desc 反序列化复杂Collection如List<Bean>,先使用函数createCollectionType构造类型，然后调用本函数
     * @author free.zhang
     * @date 2018/5/16 16:14
     */
    @SuppressWarnings("unchecked")
    public <T> T toObject(String jsonString, JavaType javaType) {

        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param '[jsonString, object]
     * @return T
     * @class_name BananaJsonMapper
     * @method update
     * @desc 当json里只含有Bean的部分属性时，更新一个已存在的bean,只覆盖该部分的属性
     * @author free.zhang
     * @date 2018/5/16 16:11
     */
    @SuppressWarnings("unchecked")
    public <T> T update(String jsonString, T object) {

        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param '[functionName, object]
     * @return java.lang.String
     * @class_name BananaJsonMapper
     * @method toJsonP
     * @desc 输出json格式数据
     * @author free.zhang
     * @date 2018/5/16 16:12
     */
    public String toJsonP(String functionName, Object object) {

        return toJson(new JSONPObject(functionName, object));
    }


    /**
     * @param '[]
     * @return com.fasterxml.jackson.databind.ObjectMapper
     * @class_name BananaJsonMapper
     * @method getMapper
     * @desc 取出mapper做进一步的设置或使用其他序列化API
     * @author free.zhang
     * @date 2018/5/16 16:13
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    public static BananaJsonMapper getDefault() {
        if (defaultMapper == null) {
            defaultMapper = new BananaJsonMapper();
            defaultMapper.enumAndStringConvert(true);
            defaultMapper.dateAndTimestampConvert(true);
        }
        return defaultMapper;
    }

}
