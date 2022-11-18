package ru.practicum.ewm.geo.yandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    @JsonProperty("GeoObjectCollection")
    public GeoObjectCollection geoObjectCollection;
}
