package com.kitchenapp.kitchenappapi.error;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiError {

    private String errorMessage;
    private List<String> validationErrors;
    private HttpStatus httpStatus;
    private int httpStatusCode;

    public int getHttpStatusCode() {
        return httpStatus != null ? httpStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

}
