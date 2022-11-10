package ru.practicum.ewm.common.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import java.util.Collections;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(
            {HttpMessageConversionException.class, ServletException.class, MethodArgumentNotValidException.class}
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageConversionException(Exception e) {
        return ApiError.of(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.BAD_REQUEST.name(),
                Collections.emptyList()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException e) {
        return ApiError.of(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN.name(),
                Collections.emptyList()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        return ApiError.of(
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND.name(),
                Collections.emptyList()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        return ApiError.of(
                e.getCause().getMessage(),
                "Integrity constraint has been violated",
                HttpStatus.CONFLICT.name(),
                Collections.emptyList()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleOtherException(Exception e) {
        return ApiError.of(
                e.getMessage(),
                "Error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                Collections.emptyList()
        );
    }
}