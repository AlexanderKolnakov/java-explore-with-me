package ru.practicum.server.stats;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.dto.stats.ViewsStatsRequsetDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class StatsServerClient {

    private final StatsServer statsServer;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveHit(@RequestBody @Valid EndpointHitDto endpointHit) {
        log.info("Передан POST запрос на Сохранение информации о том, что к эндпоиту Maim сервиса был отправлен запрос");
        return statsServer.addEndpointHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getViewsStats(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false) boolean unique) {
        log.info("Передан GET запрос на получение статистики по посещениям за период с " + start + " по " + end);
        return statsServer.getViewsStats(start, end, uris, unique);
    }
}