package ru.practicum.ewm.common;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatsClient {
    RestTemplate restTemplate;

    @Autowired
    public StatsClient(@Value("${prop.stats.url}") String statsServerUrl, RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsServerUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void save(EndpointHit hit) {
        final HttpEntity<EndpointHit> requestEntity = new HttpEntity<>(hit);
        restTemplate.postForEntity("/hit", requestEntity, EndpointHit.class);
    }

    public EndpointHit[] get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean uniq) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return restTemplate.getForEntity(
                    "/stats?start={start}&end={end}&uris={uris}&unique={unique}", EndpointHit[].class,
                    start.format(formatter),
                    end.format(formatter),
                    String.join(",", uris),
                    uniq).getBody();
        } catch (RuntimeException e) {
            return new EndpointHit[0];
        }
    }

    public EndpointHit[] get(LocalDateTime start, LocalDateTime end, List<String> uris) {

        return get(start, end, uris, false);
    }
}
