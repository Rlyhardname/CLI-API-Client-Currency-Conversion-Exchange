package com.appolica.assessment.io;

import com.appolica.assessment.models.Container;
import com.appolica.assessment.objectmapper.JsonMapper;

import java.util.List;

public interface Writer {
    void write(JsonMapper jsonMapper, String fileName, List<Container> containers);

    void write(JsonMapper jsonMapper, String fileName, Container containers);
}
