package edu.java.bot;

import edu.java.bot.dto.ApiErrorResponse;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class BotExceptionHandler {
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequest(Exception exception, WebRequest request) {
        return createApiErrorResponse(exception, request, HttpStatus.BAD_REQUEST);
    }

    private static String[] getStacktrace(Exception exception) {
        return Arrays.stream(exception.getStackTrace()).map(Objects::toString).toArray(String[]::new);
    }

    private static ApiErrorResponse createApiErrorResponse(Exception exception, WebRequest request,
        HttpStatus requestStatus) {
        return new ApiErrorResponse(
            request.getDescription(false),
            requestStatus.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            getStacktrace(exception));
    }
}
