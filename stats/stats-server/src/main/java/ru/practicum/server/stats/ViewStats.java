package ru.practicum.server.stats;


import lombok.*;


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
