package ru.practicum.ewm.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class EndpointHit {
    String app;
    String uri;
    String ip;
}
