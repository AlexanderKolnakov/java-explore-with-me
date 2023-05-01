package ru.practicum.ewm.stats;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.EndpointHitDto;

@Component
public class HitMapper {
    public static EndpointHit dtoToEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setTimestamp(endpointHitDto.getTimestamp());
        return endpointHit;
    }
}


