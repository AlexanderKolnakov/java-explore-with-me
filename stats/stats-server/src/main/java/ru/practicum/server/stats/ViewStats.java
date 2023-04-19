package ru.practicum.server.stats;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;


@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ViewStats {

    private Long hits;

    private String app;

    private String uri;

}
