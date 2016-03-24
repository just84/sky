package utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * Created by yibin on 16/3/23.
 */
public final class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    private JsonUtils() {
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static String serialize(Object data) {
        try {
            if (null == data) {
                return "";
            }
            return mapper.writeValueAsString(data);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T deSerialize(String content, Class<T> clazz) {
        try {
            return mapper.readValue(content, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T deSerialize(String content, TypeReference<T> type) {
        try {
            return mapper.readValue(content, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T convertValue(Object object, Class<T> clazz) {
        try {
            return mapper.convertValue(object, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
