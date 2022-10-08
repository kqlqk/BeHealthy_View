package me.kqlqk.behealthy.view.feign_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        Map<String, String> info;
        try (InputStream body = response.body().asInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            info = objectMapper.readValue(body, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (response.status()) {
            case 400:
                return new RuntimeException(info.get("info") != null ? info.get("info") : "Bad request");
            case 404:
                return new RuntimeException(info.get("info") != null ? info.get("info") : "Page not found");
            default:
                return errorDecoder.decode(s, response);
        }
    }
}
