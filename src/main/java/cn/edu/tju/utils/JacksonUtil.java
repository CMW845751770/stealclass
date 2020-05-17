package cn.edu.tju.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class JacksonUtil {

    private static ObjectMapper mapper  ;

    static{
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        TimeZone china = TimeZone.getTimeZone("GMT+08:00");
        mapper.setTimeZone(china);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL) ;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public static synchronized  String bean2Json(Object object)
    {
        try {
            return mapper.writeValueAsString(object) ;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null ;
        }
    }

    public static synchronized <T> T json2Bean(String jsonStr , Class<T> objClass)
    {
        try {
            return mapper.readValue(jsonStr,objClass) ;
        } catch (IOException e) {
            e.printStackTrace();
            return null ;
        }
    }

    public static synchronized <T> T  json2BeanT(String jsonStr, TypeReference<T> typeReference)
    {
        try {
            return mapper.readValue(jsonStr,typeReference) ;
        } catch (IOException e) {
            e.printStackTrace();
            return null ;
        }
    }
}
