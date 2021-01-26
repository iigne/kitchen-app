package com.kitchenapp.kitchenappapi.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ApiError {

    private final String errorMessage;
    private final List<String> validationErrors;
    private final HttpStatus httpStatus;
    private final int httpStatusCode;

    public int getHttpStatusCode() {
        return httpStatus != null ? httpStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

}
