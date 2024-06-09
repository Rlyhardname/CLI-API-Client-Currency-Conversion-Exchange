package com.appolica.assessment.models.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class ResultsDTO {
    private Map<String, String> unrecognizedFields;
    public ResultsDTO() {
        this.unrecognizedFields = new HashMap<>();
    }

    @JsonAnySetter
    public void addUnrecognizedField(String key, String value) {
        unrecognizedFields.put(key, value);
    }

    public Map<String, String> getUnrecognizedFields() {
        return unrecognizedFields;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        unrecognizedFields.forEach((k, v) -> sb.append(k)
                .append("=")
                .append("'")
                .append(v)
                .append("'")
                .append(System.lineSeparator()));
        return "{" +
                sb +
                '}';
    }
}
