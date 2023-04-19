package ru.practicum.server.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select h from EndpointHit h " +
            "where h.uri =?3 and h.timestamp > ?1 and h.timestamp < ?2")
    List<EndpointHit> searchStats(LocalDateTime start, LocalDateTime end, String uris);


    @Query(value = "select new ru.practicum.server.stats.ViewStats(COUNT (distinct HH.uri) , HH.app , HH.uri) " +
            "from EndpointHit HH " +
            "where HH.timestamp > ?1 and HH.timestamp < ?2 " +
            "group by HH.uri, HH.app")
    List<ViewStats> searchStatsForAllUri(LocalDateTime start, LocalDateTime end);
}
