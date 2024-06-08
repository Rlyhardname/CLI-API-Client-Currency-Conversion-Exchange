package com.appolica.assessment.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class ApiKeyReader implements Reader<String> {
    @Override
    public String read(String API_KEY_PATH){
        String predicateValue = "api";
        try (BufferedReader br = Files.newBufferedReader(Path.of(API_KEY_PATH))) {
            String key = br.lines().filter(x -> x.contains(predicateValue)).collect(Collectors.joining());
            return key.split(":")[1].replace("\"", "").trim();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
