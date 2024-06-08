package com.appolica.assessment.network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.appolica.assessment.messages.ValidationMessages.REQUEST_ERROR;

public class CustomHttpClient {
    public HttpResponse<String> sendRequest(String url) throws NullPointerException,IllegalArgumentException  {
        HttpRequest request = buildRequest(url);
        return getResponse(request);
    }

    private HttpRequest buildRequest(String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    private HttpResponse<String> getResponse(HttpRequest request) {
        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.err.println(REQUEST_ERROR);
            throw new RuntimeException(e);
        }

    }

}
