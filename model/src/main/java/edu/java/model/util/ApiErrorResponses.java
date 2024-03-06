package edu.java.model.util;

import edu.java.model.response.ApiErrorResponse;
import java.util.Arrays;

public class ApiErrorResponses {

    private ApiErrorResponses() {
    }

    public static ApiErrorResponse of(Exception e, Integer code, String description) {
        return ApiErrorResponse.builder()
            .code(code.toString())
            .description(description)
            .exceptionName(e.getClass().getName())
            .exceptionMessage(e.getMessage())
            .stacktrace(Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList())
            .build();
    }
}
