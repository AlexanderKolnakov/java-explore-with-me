package ru.practicum.ewm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
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
    public ErrorResponse error400(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse error404(final EntityNotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse error409(final DataIntegrityViolationException e) {
        log.info("409 {}", e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.toString(),
                "Incorrectly made request.",
                e.getMessage());
    }

    @Value
    @Builder
    @AllArgsConstructor
    @Jacksonized
    public static class ErrorResponse {
        String status;
        String reason;
        String message;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp = LocalDateTime.now();
    }
}
