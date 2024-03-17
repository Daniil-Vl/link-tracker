package edu.java.exceptionhandler;

import edu.java.ApiErrorResponse;
import edu.java.exceptions.ChatNotExistException;
import edu.java.exceptions.LinkNotExistException;
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
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            "HandlerMethodValidationException",
            Arrays.toString(exc.getDetailMessageArguments()),
            Arrays.stream(exc.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(value = {ChatNotExistException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse chatNotFound(ChatNotExistException e) {
        log.warn("Catch ChatNotExistException");

        return new ApiErrorResponse(
            "Chat doesn't exist",
            String.valueOf(HttpStatus.NOT_FOUND.value()),
            "ChatNotExistException",
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(value = {LinkNotExistException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse linkNotFound(LinkNotExistException e) {
        log.warn("Catch LinkNotExistException");

        return new ApiErrorResponse(
            "Link doesn't exist",
            String.valueOf(HttpStatus.NOT_FOUND.value()),
            "LinkNotExistException",
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
