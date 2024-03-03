package edu.java.bot.exceptionhandler;

import edu.java.ApiErrorResponse;
import java.util.Arrays;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class BotControllerExceptionHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse invalidArgumentsException(MethodArgumentNotValidException exc) {
        String exceptionMessage = buildExceptionMessage(exc);

        log.warn("Catch MethodArgumentNotValidException");
        log.warn("Exception message: " + exceptionMessage);

        return new ApiErrorResponse(
            "Invalid argument in the request body",
            String.valueOf(exc.getStatusCode().value()),
            exc.getClass().getSimpleName(),
            exceptionMessage,
            Arrays.stream(exc.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    private String buildExceptionMessage(MethodArgumentNotValidException exc) {
        return String.join(
            " ",
            exc.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList()
        );
    }
}
