package com.appolica.assessment.io;

import com.appolica.assessment.models.Container;
import com.appolica.assessment.objectmapper.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonFileWriter implements Writer {
    @Override
    public void write(JsonMapper jsonMapper, String fileName, List<Container> containers) {
        try {
            jsonMapper.getMapper().writeValue(new File("src//main//resources//" + fileName + ".json"), containers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(JsonMapper jsonMapper, String fileName, Container containers) {
        try {
            jsonMapper.getMapper().writeValue(new File("src//main//resources//" + fileName + ".json"), containers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
