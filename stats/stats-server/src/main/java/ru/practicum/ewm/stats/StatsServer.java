package ru.practicum.ewm.stats;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHitDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class StatsServer {

    private final StatsRepository statsRepository;

    @Transactional(rollbackOn = Exception.class)
    public EndpointHitDto addEndpointHit(EndpointHitDto endpointHit) {
        statsRepository.save(HitMapper.dtoToEndpointHit(endpointHit));
        return endpointHit;
    }

    public List<ViewStats> getViewsStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> resultListViewStats = new ArrayList<>();

        if (unique) {
            if (!uris.isEmpty()) {

                for (String uri : uris) {
                    List<ViewStats> listViewStatsWithUniqIp = statsRepository.searchStatsWithUniqIp(start, end, uri);
                    resultListViewStats.addAll(listViewStatsWithUniqIp);
                }
            } else {
                resultListViewStats = statsRepository.searchStatsWithUniqIpByAllUris(start, end);
            }
        } else {
            if (!uris.isEmpty()) {
                for (String uri : uris) {
                    List<EndpointHit> listHitByOneUri = statsRepository.searchStats(start, end, uri)
                            .orElse(Collections.emptyList());
                    if (!listHitByOneUri.isEmpty()) {
                        ViewStats viewStats = new ViewStats();
                        viewStats.setApp(listHitByOneUri.get(0).getApp());
                        viewStats.setUri(uri);
                        viewStats.setHits((long) listHitByOneUri.size());

                        resultListViewStats.add(viewStats);
                    }
                }
            } else {
                resultListViewStats = statsRepository.searchStatsForAllUri(start, end);
            }
        }
        if (!resultListViewStats.isEmpty()) {
            return resultListViewStats.stream()
                    .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}


