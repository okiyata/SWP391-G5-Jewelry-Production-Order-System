package com.swp391.JewelryProduction.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private HttpStatus status;
    private String message;
    private Map<String, String> responseList;

    public static class ResponseBuilder {
        public ResponseBuilder response(String key, String value) {
            if (responseList == null) responseList = new HashMap<>();
            responseList.put(key, value);
            return this;
        }
    }
}
