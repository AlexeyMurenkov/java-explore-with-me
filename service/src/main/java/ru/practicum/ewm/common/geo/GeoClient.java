package ru.practicum.ewm.common.geo;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.Location;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GeoClient {
    RestTemplate restTemplate;

    @Autowired
    public GeoClient(@Value("${prop.geo.url}") String statsServerUrl, RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsServerUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public Location patch(Location location) {
        try {
            return restTemplate.patchForObject("/geo", location, Location.class);
        } catch (RuntimeException e) {
            return location;
        }
    }
}
