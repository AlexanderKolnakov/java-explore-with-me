package ru.practicum.mainserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingRequestHeaderException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError error400(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiError error404(final EntityNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ApiError(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ApiError error409(final DataIntegrityViolationException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

//    @ExceptionHandler(EntityNotFoundException.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ApiError error500(final EntityNotFoundException e) {
//        log.info("500 {}", e.getMessage());
//        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
//                "Incorrectly made request.",
//                e.getMessage(),
//                LocalDateTime.now());
//    }
}
