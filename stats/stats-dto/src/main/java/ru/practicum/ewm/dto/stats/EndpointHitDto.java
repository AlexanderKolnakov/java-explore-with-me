package ru.practicum.ewm.dto.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
//@Entity
//@Table(name = "HITS")
public class EndpointHitDto {

    private Long id;

    @NotBlank(message = "app не может быть пустым или Null")
    private String app;

    @NotBlank(message = "uri не может быть пустым или Null")
    private String uri;

    @NotBlank(message = "ip не может быть пустым или Null")
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}
