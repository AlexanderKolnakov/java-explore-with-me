package ru.practicum.mainserver.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiError extends RuntimeException {

    // Сведения об ошибке

    //    private String errors;   // Список стектрейсов или описания ошибок

    private String status;   // Код статуса HTTP-ответа

    private String reason;   // Общее описание причины ошибки

    private String message;   // Сообщение об ошибке

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;   // Дата и время когда произошла ошибка
}
