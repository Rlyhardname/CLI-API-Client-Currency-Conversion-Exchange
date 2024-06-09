package com.appolica.assessment.objectmapper;

import com.appolica.assessment.models.Container;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface JsonMapper {

    <T extends Container> T mapToContainer(String JsonString, Class<? extends Container> container);

    ObjectMapper getMapper();
}
