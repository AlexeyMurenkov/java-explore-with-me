package ru.practicum.ewm.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.geo.GeoClientPatcher;
import ru.practicum.ewm.common.validation.group.Create;
import ru.practicum.ewm.common.validation.group.Update;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventGetDto;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.RequestDto;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class UserEventController {
    EventService eventService;
    GeoClientPatcher geoClientPatcher;

    @GetMapping("/{userId}/events")
    public Collection<EventGetDto> getEventsByUserId(@PathVariable @NotNull Long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.debug("GET events by user id={} from={} size={}", userId, from, size);
        return eventService.getEventsByUserId(userId, from, size).stream()
                .map(geoClientPatcher::patchEventGetDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{userId}/events")
    public EventGetDto updateEvent(@PathVariable @NotNull Long userId,
                                   @RequestBody @Validated(Update.class) @NotNull EventDto eventDto) {
        log.debug("PATCH request to update event {} by user id={}", eventDto, userId);
        return geoClientPatcher.patchEventGetDto(eventService.updateEvent(userId,
                geoClientPatcher.patchEventDto(eventDto)));
    }

    @PostMapping("/{userId}/events")
    public EventGetDto createEvent(@PathVariable @NotNull Long userId,
                                   @RequestBody @Validated(Create.class) @NotNull EventDto eventDto) {
        log.debug("POST request to create event {} by user id={}", eventDto, userId);
        return geoClientPatcher.patchEventGetDto(eventService.createEvent(userId,
                geoClientPatcher.patchEventDto(eventDto)));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventGetDto getEventByIdAndUserId(@PathVariable @NotNull Long eventId, @PathVariable @NotNull Long userId) {
        log.debug("GET event eventId={} by user id={}", eventId, userId);
        return geoClientPatcher.patchEventGetDto(eventService.getEventByIdAndUserId(eventId, userId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventGetDto cancelEventIdAndUserId(@PathVariable @NotNull Long eventId, @PathVariable @NotNull Long userId) {
        log.debug("PATCH request to cancel event for eventId={} by user id={}", eventId, userId);
        return geoClientPatcher.patchEventGetDto(eventService.cancelEventByIdAndUserId(eventId, userId));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<RequestDto> getRequestsByUserIdAndEventId(@PathVariable @NotNull Long userId,
                                                                @PathVariable @NotNull Long eventId) {
        log.debug("GET requests for eventId={} by user id={}", eventId, userId);
        return eventService.getRequestsByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId,
                                     @PathVariable @NotNull Long reqId) {
        log.debug("PATCH to confirm request reqId={} for eventId={} by user id={}", reqId, eventId, userId);
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId,
                                     @PathVariable @NotNull Long reqId) {
        log.debug("PATCH to reject request reqId={} for eventId={} by user id={}", reqId, eventId, userId);
        return eventService.rejectRequest(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<RequestDto> getRequests(@PathVariable @NotNull Long userId) {
        log.debug("GET requests by user id={}", userId);
        return eventService.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public RequestDto createRequest(@PathVariable @NotNull Long userId, @RequestParam @NotNull Long eventId) {
        log.debug("POST to create request for eventId={} by user id={}", eventId, userId);
        return eventService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{reqId}/cancel")
    public RequestDto cancelRequest(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long reqId) {
        log.debug("PATCH to cancel request for reqId={} by user id={}", reqId, userId);
        return eventService.cancelRequest(userId, reqId);
    }
}
