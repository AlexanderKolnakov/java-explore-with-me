package ru.practicum.server.stats;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.stats.EndpointHitDto;

@Component
public class HitMapper {

    public static EndpointHit dtoToEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getId(),
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp()
        );
    }
}


