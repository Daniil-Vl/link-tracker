package edu.java.exceptionhandler;

import edu.java.ApiErrorResponse;
import java.util.Arrays;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Log4j2
@RestControllerAdvice
public class ScrapperControllerExceptionHandler {
    @ExceptionHandler(value = {HandlerMethodValidationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse invalidArgumentsException(HandlerMethodValidationException exc) {
        log.warn("Catch HandlerMethodValidationException");

        return new ApiErrorResponse(
            "Invalid arguments in path or header",
            "400",
            "HandlerMethodValidationException",
            Arrays.toString(exc.getDetailMessageArguments()),
            Arrays.stream(exc.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
