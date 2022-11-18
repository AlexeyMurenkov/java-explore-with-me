package ru.practicum.ewm.geo.yandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatureMember {
    @JsonProperty("GeoObject")
    public GeoObject geoObject;
}
