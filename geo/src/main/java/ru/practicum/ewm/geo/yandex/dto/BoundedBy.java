package ru.practicum.ewm.geo.yandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BoundedBy {
    @JsonProperty("Envelope")
    public Envelope envelope;
}
