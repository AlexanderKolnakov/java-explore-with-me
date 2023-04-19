package ru.practicum.server.stats;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.dto.stats.ViewsStatsRequsetDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StatsServer {

    private final StatsRepository statsRepository;

    public EndpointHitDto addEndpointHit(EndpointHitDto endpointHit) {
        statsRepository.save(HitMapper.dtoToEndpointHit(endpointHit));
        return endpointHit;
    }

    public List<ViewStats> getViewsStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        List<ViewStats> resultListViewStats = new ArrayList<>();

        if (uris != null) {

            for (String uri : uris) {

                List<EndpointHit> listHitByOneUri = statsRepository.searchStats(start, end, uri);

                ViewStats viewStats = new ViewStats();
                viewStats.setApp(listHitByOneUri.get(0).getApp());
                viewStats.setUri(uri);
                viewStats.setHits((long) listHitByOneUri.size());

                resultListViewStats.add(viewStats);
            }
        } else {
            resultListViewStats = statsRepository.searchStatsForAllUri(start, end);
        }
        return resultListViewStats;
    }
}
