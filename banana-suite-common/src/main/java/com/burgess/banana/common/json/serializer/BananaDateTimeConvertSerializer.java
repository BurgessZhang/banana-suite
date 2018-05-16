package com.burgess.banana.common.json.serializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.json.serializer
 * @file BananaDateTimeConvertSerializer.java
 * @time 2018-05-16 16:23
 * @desc 日期格式序列化
 */
public class BananaDateTimeConvertSerializer extends JsonSerializer<Date> {

    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void serialize(Date date, JsonGenerator jgen, SerializerProvider provider) throws JsonProcessingException {
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            jgen.writeString(dateFormat.format(date));
        } catch (IOException e) {
            throw new RuntimeException("Date转换json异常，格式：" + pattern);
        }
    }
}
