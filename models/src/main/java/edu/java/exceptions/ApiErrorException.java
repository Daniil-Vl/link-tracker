package edu.java.exceptions;

import edu.java.ApiErrorResponse;

public class ApiErrorException extends RuntimeException {
    final ApiErrorResponse apiErrorResponse;

    public ApiErrorException(ApiErrorResponse response) {
        super(response.exceptionMessage());
        apiErrorResponse = response;
    }
}
