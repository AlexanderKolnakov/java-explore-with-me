package ru.practicum.ewm.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select h from EndpointHit h " +
            "where h.uri =?3 and h.timestamp > ?1 and h.timestamp < ?2")
    Optional<List<EndpointHit>> searchStats(LocalDateTime start, LocalDateTime end, String uris);


    @Query(value = "select new ru.practicum.ewm.stats.ViewStats(COUNT (distinct h.uri) , h.app , h.uri) " +
            "from EndpointHit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.uri, h.app")
    List<ViewStats> searchStatsForAllUri(LocalDateTime start, LocalDateTime end);


    @Query(value = "select new ru.practicum.ewm.stats.ViewStats(COUNT (distinct h.uri) , h.app , h.uri) " +
            "from EndpointHit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.ip, h.app, h.uri")
    List<ViewStats> searchStatsWithUniqIpByAllUris(LocalDateTime start, LocalDateTime end);


    @Query(value = "select new ru.practicum.ewm.stats.ViewStats(COUNT (distinct h.uri) , h.app , h.uri) " +
            "from EndpointHit h " +
            "where h.uri = ?3 and h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.ip, h.app, h.uri")
    List<ViewStats> searchStatsWithUniqIp(LocalDateTime start, LocalDateTime end, String uris);
}
