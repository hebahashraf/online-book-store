package com.example.book.util;

import com.example.book.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Utility class for serializing / deserializing json
 */
@Slf4j
public final class JsonTool {

    private final ObjectMapper objectMapper;

    private JsonTool(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static JsonTool custom(ObjectMapper objectMapper) {
        return new JsonTool(objectMapper);
    }

    /**
     * @param json the json string to be deserialized
     * @param tClass the type of class to be deserialized into
     * @return object of type T
     */
    public <T> T load(String json, Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            log.error("Error processing json string - {}; Error message : {}", json, e.getMessage(), e);
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    /**
     * @param jsonObj the object to be serialized to json
     * @return json string
     */
    public String toJson(Object jsonObj) {
        try {
            return objectMapper.writeValueAsString(jsonObj);
        } catch (JsonProcessingException e) {
            log.error("Error converting to json string - {}", e.getMessage(), e);
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
