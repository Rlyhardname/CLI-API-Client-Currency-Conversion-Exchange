package com.appolica.assessment.objectmapper;

import com.appolica.assessment.models.Container;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectMapper implements JsonMapper {
    private final ObjectMapper objectMapper;

    public JsonObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        objectMapper.findAndRegisterModules();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Container> T mapToContainer(String JsonString, Class<? extends Container> container) {
        try {
            return (T) objectMapper.readValue(JsonString, container);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ObjectMapper getMapper() {
        return objectMapper;
    }

}
