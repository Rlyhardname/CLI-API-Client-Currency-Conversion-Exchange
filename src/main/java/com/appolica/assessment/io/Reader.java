package com.appolica.assessment.io;

public interface Reader<T> {
    T read(String pathToResource);
}
