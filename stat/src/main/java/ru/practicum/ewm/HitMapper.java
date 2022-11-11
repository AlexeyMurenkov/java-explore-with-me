package ru.practicum.ewm;

import ru.practicum.ewm.dto.EndpointHit;

import java.time.LocalDateTime;

public class HitMapper {
    public static Hit fromEndpointHit(EndpointHit endpointHit) {
        return Hit.of(
                null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                LocalDateTime.now()
        );
    }
}
