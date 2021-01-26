package com.kitchenapp.kitchenappapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException e) {
        final ApiError apiError = ApiError.builder()
                .errorMessage("Something went wrong...")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return createResponse(apiError);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> validationErrors = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        final ApiError apiError = ApiError.builder()
                .errorMessage("Some fields contain invalid values")
                .validationErrors(validationErrors)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return createResponse(apiError);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleDatabaseError(EntityNotFoundException e) {
        final ApiError apiError = ApiError.builder()
                .errorMessage("The value was not found in the database")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        return createResponse(apiError);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> handleError(Exception e) {
        final ApiError apiError = ApiError.builder()
                .errorMessage("An internal error has occurred...")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return createResponse(apiError);
    }

    private static ResponseEntity<ApiError> createResponse(final ApiError apiError) {
        return ResponseEntity.status(apiError.getHttpStatusCode()).body(apiError);
    }
}
