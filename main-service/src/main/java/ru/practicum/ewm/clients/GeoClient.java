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
            String countryCode = "Данная локация не принадлежит ни одной из стран";
            if(addressJson.get("country_code") != null) {
                countryCode = (String) addressJson.get("country_code");
            }
            JSONArray componentsJsonArray = (JSONArray) addressJson.get("Components");
            String city = "Данная локация не входит ни в какой город";
            if (componentsJsonArray.size() >= 4) {
                JSONObject cityObject = (JSONObject) componentsJsonArray.get(3);
                city = (String) cityObject.get("name");
            }
            location.setText(text);
            location.setCity(city);
            location.setCountryCode(countryCode);
            location.setLat(lat);
            location.setLon(lon);
        } catch (ParseException e) {
            System.out.println("Parsing Error " + e.getMessage());
        }
        return location;
    }
}

