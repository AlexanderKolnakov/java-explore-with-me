//package ru.practicum.ewm.client.stats;
//
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//import ru.practicum.ewm.dto.stats.EndpointHit;
//import ru.practicum.ewm.dto.stats.ViewsStatsRequset;
//
//import javax.validation.Valid;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@Slf4j
//@AllArgsConstructor
//public class StatsClient {
//
//    private final StatsServer statsServer;
//
//    @PostMapping("/hit")
//    public EndpointHit saveHit(@RequestBody @Valid EndpointHit endpointHit) {
//        log.debug("Передан POST запрос на Сохранение информации о том, что к эндпоиту Maim сервиса был отправлен запрос");
//        return statsServer.addEndpointHit(endpointHit);
//    }
//
//    @GetMapping("/stats")
//    public List<ViewsStatsRequset> getViewsStats(@RequestParam(required = false) String start,
//                                                 @RequestParam(required = false) String end,
//                                                 @RequestParam(required = false) List<String> uris,
//                                                 @RequestParam(required = false) boolean unique)
//    {
//        log.debug("Передан GET запрос на получение статистики по посещениям за период с " + start + " по " + end);
//        return statsServer.getViewsStats(start, end, uris, unique);
//    }
//}
