package com.solvd.hospital.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonFileHandler {
    private final ObjectMapper objectMapper;

    public JsonFileHandler() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> List<T> readFromJson(String path, Class<T> tClass) throws IOException {
        try {
            File file = new File(path);
            JsonNode jsonNode = objectMapper.readTree(file);

            if (jsonNode.isArray()) {
                return objectMapper.readValue(
                        file,
                        objectMapper.getTypeFactory()
                                .constructCollectionType(List.class, tClass)
                );
            } else {
                T singleObject = objectMapper.readValue(
                        file,
                        objectMapper.getTypeFactory().constructType(tClass));
                return List.of(singleObject);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
