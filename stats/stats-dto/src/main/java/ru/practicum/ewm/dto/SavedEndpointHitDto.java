package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
@AllArgsConstructor
public class SavedEndpointHitDto {

    private Long id;  // Идентификатор записи

    private String app;  // Идентификатор сервиса для которого записывается информация (ewm-main-service)

    private String uri;   // URI для которого был осуществлен запрос

    private String ip;   // IP-адрес пользователя, осуществившего запрос

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
