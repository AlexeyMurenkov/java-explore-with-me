package ru.practicum.ewm.geo.yandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoObject {
    public String name;
    public BoundedBy boundedBy;
    @JsonProperty("Point")
    public Point point;
}
