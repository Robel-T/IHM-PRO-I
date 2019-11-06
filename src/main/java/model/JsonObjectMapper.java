package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Wrapper Class to call ObjectMapper methods
 * to convert Json to POJO and inverse
 */
public class JsonObjectMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJson(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json, type);
    }

    public static String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
