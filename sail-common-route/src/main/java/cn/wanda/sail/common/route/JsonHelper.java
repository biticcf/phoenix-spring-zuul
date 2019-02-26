package cn.wanda.sail.common.route;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @Author: DanielCao
 * @Date:   2017年10月29日
 * @Time:   下午8:07:21
 *
 */
public class JsonHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);

    public final static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode toTree(String json) throws IOException {
        return MAPPER.readTree(json);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String json) throws IOException {
        if (StringUtils.isEmpty(json)) {
            return new HashMap<String, Object>(0);
        }
        return MAPPER.readValue(json, Map.class);
    }

    public static <T> T toBean(String json, Class<T> clazz) throws IOException {
        if (json == null || "".equals(json)) return null;
        JsonParser parser = JSON_FACTORY.createParser(json);
        parser.setCodec(MAPPER);
        T t = parser.readValueAs(clazz);
        parser.close();
        return t;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(String json, Class<T> clazz) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(List.class, clazz, clazz);
        return (List<T>) objectMapper.readValue(json, javaType);
    }

    public static String toJson(Object object) throws IOException {
        return useMapper(object, MAPPER);
    }

    public static String toJsonWithoutExcepTion(Object object) {
        try {
            return useMapper(object, MAPPER);
        } catch (Exception e) {
            LOGGER.error("{}", e);
        }
        return null;
    }

    public static String toJsonWithoutNull(Object object) throws IOException {
        return useMapper(object, MAPPER2);
    }


    private static String useMapper(Object object, ObjectMapper mapper) throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = JSON_FACTORY.createGenerator(writer);
        generator.setCodec(mapper);
        generator.writeObject(object);
        generator.close();
        writer.close();
        return writer.toString();
    }

    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectMapper MAPPER2 = new ObjectMapper();

    static {
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER2.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setTimeZone(TimeZone.getDefault());
        MAPPER2.setTimeZone(TimeZone.getDefault());
    }

}
