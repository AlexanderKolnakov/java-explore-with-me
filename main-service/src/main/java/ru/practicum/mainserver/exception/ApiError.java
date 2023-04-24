package ru.practicum.mainserver.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiError {

    // Сведения об ошибке

    private String errors;   // Список стектрейсов или описания ошибок

    private String message;   // Сообщение об ошибке

    private String reason;   // Общее описание причины ошибки

    private String status;   // Код статуса HTTP-ответа

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;   // Дата и время когда произошла ошибка
}
