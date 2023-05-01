package ru.practicum.ewm.client.stats;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${statistic-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ViewStatsDto hit(EndpointHitDto endpointHitDto) {
        Gson gson = new Gson();
        ResponseEntity<Object> objectResponseEntity = post("/hit", endpointHitDto);
        String json = gson.toJson(objectResponseEntity.getBody());

        return gson.fromJson(json, ViewStatsDto.class);
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        Gson gson = new Gson();
        Map<String, Object> parameters = Map.of(
                "uris", String.join(",", uris),
                "unique", unique,
                "start", start,
                "end", end
        );
        ResponseEntity<Object> objectResponseEntity =
                get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        String json = gson.toJson(objectResponseEntity.getBody());
        ViewStatsDto[] viewStatDtoArray = gson.fromJson(json, ViewStatsDto[].class);

        return Arrays.asList(viewStatDtoArray);
    }
}



