package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value(staticConstructor = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
    String name;
    Float lat;
    Float lon;
}
