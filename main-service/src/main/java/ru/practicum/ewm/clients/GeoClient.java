package ru.practicum.ewm.clients;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.location.LocationInMap;

import java.util.Map;

@Service
@Slf4j
public class GeoClient extends BaseClient {

    @Autowired
    public GeoClient(@Value("https://geocode-maps.yandex.ru") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

//    public String getLocFromYandex(Float lat, Float lon) {
//        Gson gson = new Gson();
//        Map<String, Object> parameters = Map.of(
//                "lat", lat,
//                "lon", lon
//        );
//
////        37.611347,55.760241
//
//
//        ResponseEntity<Object> objectResponseEntity =
//                get("{lat},{lon}", parameters);
//
//        System.out.println(objectResponseEntity.toString());
//
//        String json = gson.toJson(objectResponseEntity.getBody());
//
//
//
//
////        ViewStatsDto[] viewStatDtoArray = gson.fromJson(json, ViewStatsDto[].class);
//        return json;
//    }

    public LocationInMap getLocFromYandex(Float lat, Float lon) {
        Gson gson = new Gson();
        Map<String, Object> parameters = Map.of(
                "lat", lat,
                "lon", lon
        );


        ResponseEntity<Object> objectResponse =
                get("/1.x/?apikey=f4ceba6c-6848-4e79-a86f-6c6ccad4a91f&geocode={lon},{lat}&format=json", parameters);


        String jsonString = gson.toJson(objectResponse.getBody());
        LocationInMap location = new LocationInMap();

        System.out.println(jsonString);

        JSONParser parser = new JSONParser();

        try {
            JSONObject rootJson = (JSONObject) parser.parse(jsonString);

            JSONObject responseJson = (JSONObject) rootJson.get("response");
            JSONObject geoObjectCollectionJson = (JSONObject) responseJson.get("GeoObjectCollection");
            JSONArray featureMemberJsonArray = (JSONArray) geoObjectCollectionJson.get("featureMember");
            JSONObject infoLocation = (JSONObject) featureMemberJsonArray.get(0);
            JSONObject geoObjectJson = (JSONObject) infoLocation.get("GeoObject");
            JSONObject metaDataPropertyJson = (JSONObject) geoObjectJson.get("metaDataProperty");
            JSONObject geocoderMetaDataJson = (JSONObject) metaDataPropertyJson.get("GeocoderMetaData");
            String text = (String) geocoderMetaDataJson.get("text");
            JSONObject addressJson = (JSONObject) geocoderMetaDataJson.get("Address");
            String countryCode = (String) addressJson.get("country_code");

            location.setText(text);
            location.setCountryCode(countryCode);
            location.setLat(lat);
            location.setLon(lon);

            System.out.println(location.toString());





        } catch (ParseException e) {
            System.out.println("Parsing Error " + e.getMessage());
        }













        return location;
    }

}

