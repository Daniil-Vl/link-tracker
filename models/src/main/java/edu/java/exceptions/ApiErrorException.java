package edu.java.exceptions;

import edu.java.ApiErrorResponse;

public class ApiErrorException extends RuntimeException {

    public ApiErrorException(ApiErrorResponse response) {
        super(response.toString());
    }
}
