package ru.practicum.ewm.event.dto;

import lombok.Value;
import ru.practicum.ewm.event.model.RequestStatus;

import java.time.LocalDateTime;

@Value(staticConstructor = "of")
public class RequestDto {
    Long id;
    Long event;
    Long requester;
    RequestStatus status;
    LocalDateTime created;
}
