package ru.practicum.ewm.dto;

import lombok.Value;

@Value
public class ViewStats {
    String app;
    String uri;
    Long hits;
}
