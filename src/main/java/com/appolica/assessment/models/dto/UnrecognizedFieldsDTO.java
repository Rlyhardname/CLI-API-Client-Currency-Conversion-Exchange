package com.appolica.assessment.models.dto;

import com.appolica.assessment.models.Container;

public class UnrecognizedFieldsDTO implements Container {
    private String fieldName;
    private String fieldValue;

    public UnrecognizedFieldsDTO(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    @Override
    public String toString() {
        return "UnrecognizedFieldsDTO{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                '}';
    }
}
