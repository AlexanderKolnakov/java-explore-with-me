package ru.practicum.server.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HITS")
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "app не может быть пустым или Null")
    private String app;

    @NotBlank(message = "uri не может быть пустым или Null")
    private String uri;

    @NotBlank(message = "ip не может быть пустым или Null")
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "TIME_STAMP")
    private LocalDateTime timestamp;

}
