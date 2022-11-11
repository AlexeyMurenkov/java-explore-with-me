package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.model.Request;

import java.util.Collection;
import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.of(
                request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus(),
                request.getCreated()
        );
    }

    public static Collection<RequestDto> toRequestDtos(Collection<Request> requests) {
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }
}
