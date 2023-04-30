package ru.practicum.ewm.client.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.stats.EndpointHitDto;

@Service
public class StatsClient extends BaseClient {

//    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    private final String application;
//    private final String statsServiceUri;
//    private final ObjectMapper json;
//    private final HttpClient httpClient;


    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    //    public ResponseEntity<Object> hit(HttpServletRequest userRequest) {
    public ResponseEntity<Object> hit(EndpointHitDto hit) {
//        EndpointHitDto hit = EndpointHitDto.builder()
//                .app("ewm-main-service")
//                .ip(userRequest.getRemoteAddr())
//                .uri(userRequest.getRequestURI())
//                .timestamp(LocalDateTime.now())
//                .build();


//        EndpointHitDto hit = EndpointHitDto.builder()
//                .app("ewm-main-service")
//                .ip(userRequest.getRemoteAddr())
//                .uri(userRequest.getRequestURI())
//                .timestamp(LocalDateTime.now())
//                .build();
        return post("/hit", hit);
    }
}


//    public void hit(HttpServletRequest userRequest) {
//        EndpointHitDto hit = EndpointHitDto.builder()
//                .app("ewm-main-service")
//                .ip(userRequest.getRemoteAddr())
//                .uri(userRequest.getRequestURI())
//                .timestamp(LocalDateTime.now())
//                .build();
//        try {
////            HttpRequest.BodyPublisher bodyPublisher = HttpRequest
////                    .BodyPublisher
//            HttpRequest.
////
////            HttpRequest hitRequest = HttpRequest.newBuilder()
////                    .
//
//            HttpResponse<Void> response = httpClient.send( )
//        }
//
//
//        ;
//
//
//    }


// из чата
//    @Autowired
//    public StatsClient(RestTemplateBuilder builder) {
//        super(builder
//                .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090"))
//                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
//                .build()
//        );
//    }
//
//    @Autowired
//    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
//                         RestTemplateBuilder builder) {
//        super(builder
//                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
//                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
//                .build()
//        );
//    }


//    private static final DateTimeFormatter DTF =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////    private final String application;
////    private final String statsServiceUri;
//    private final ObjectMapper json;
//    private final HttpClient httpClient;

//    public StatsClient(ObjectMapper json) {
//        this.json = json;
//    }



