package ru.practicum.ewm.geo.yandex;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.Location;
import ru.practicum.ewm.geo.GeoClient;
import ru.practicum.ewm.geo.Geo;
import ru.practicum.ewm.geo.yandex.dto.YandexGeoDto;
import ru.practicum.ewm.geo.yandex.dto.GeoObject;

import java.util.Arrays;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class YandexGeoClient implements GeoClient {
    RestTemplate restTemplate;

    String apikey;

    @Autowired
    public YandexGeoClient(@Value("${yandex.geo.url}") String statsServerUrl,
                           RestTemplateBuilder restTemplateBuilder, @Value("${yandex.geo.apikey}") String apikey) {
        restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsServerUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        this.apikey = apikey;
    }

    private static Location parseLocation(String pos) {
        if (pos == null) return Location.of(pos, 0.0F, 0.0F);
        final Float[] splited = Arrays.stream(pos.split(" "))
                .map(Float::parseFloat)
                .toArray(Float[]::new);
        if (splited.length < 2) return Location.of(pos, 0.0F, 0.0F);
        return Location.of(pos, splited[0], splited[1]);
    }

    private YandexGeoDto get(String geocode) {
        final ResponseEntity<YandexGeoDto> response = restTemplate.getForEntity(
                "/1.x/?format=json&results=1&sco=latlong&apikey={apikey}&geocode={geocode}", YandexGeoDto.class,
                apikey, geocode);
        return response.getBody();
    }

    @Override
    public Geo getLocation(String geocode) {
        final YandexGeoDto geoDto = get(geocode);
        final Geo geo;
        if (geoDto != null && geoDto.response.geoObjectCollection.featureMember.length > 0) {
            final GeoObject geoObject = geoDto.response.geoObjectCollection.featureMember[0].geoObject;
            geo = Geo.of(
                    null,
                    geocode,
                    geoObject.name,
                    parseLocation(geoObject.point.pos).getLat(),
                    parseLocation(geoObject.point.pos).getLon(),
                    parseLocation(geoObject.boundedBy.envelope.lowerCorner).getLat(),
                    parseLocation(geoObject.boundedBy.envelope.lowerCorner).getLon(),
                    parseLocation(geoObject.boundedBy.envelope.upperCorner).getLat(),
                    parseLocation(geoObject.boundedBy.envelope.upperCorner).getLon()
            );
        } else {
            geo = null;
        }
        return geo;
    }
}
