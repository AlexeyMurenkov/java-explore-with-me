package ru.practicum.ewm.event.dto;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Value(staticConstructor = "of")
public class Location {

    @Min(-90)
    @Max(90)
    float lat;

    @Min(-180)
    @Max(180)
    float lon;
}
