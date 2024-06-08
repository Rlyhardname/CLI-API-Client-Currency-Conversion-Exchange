package com.appolica.assessment.parser;

public interface Parser<T> {
    T parse(String line);
}
