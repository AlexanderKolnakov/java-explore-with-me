package ru.practicum.ewm;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient {

//    Для локальной работы - ("http://localhost:9090")
//    Для контейнеров      - ("http://stats-server:9090")
    @Autowired
    public StatsClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void hit(EndpointHitDto endpointHitDto) {
        post("/hit", endpointHitDto);
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris) {
        Gson gson = new Gson();
        Map<String, Object> parameters = Map.of(
                "uris", String.join(",", uris),
                "unique", false,
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



