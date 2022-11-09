package ru.practicum.ewm.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventGetDto;
import ru.practicum.ewm.event.model.EventState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventController {

    EventService eventService;

    @GetMapping
    public Collection<EventGetDto> getEvents(@RequestParam(required = false) Long[] users,
                                             @RequestParam(required = false) EventState[] states,
                                             @RequestParam(required = false) Long[] categories,
                                             @RequestParam(required = false)
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 Optional<LocalDateTime> rangeStart,
                                             @RequestParam(required = false)
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 Optional<LocalDateTime> rangeEnd,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.debug("GET request filtered events by admin from={} size={}", from, size);
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventGetDto updateEvent(@PathVariable @NotNull Long eventId,
                                   @RequestBody @NotNull EventDto eventDto) {
        log.debug("PUT request for update events by admin eventId={}", eventId);
        return eventService.updateEventByAdmin(eventId, eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventGetDto publishEvent(@PathVariable @NotNull Long eventId) {
        log.debug("PATCH request for publish event by admin eventId={}", eventId);
        return eventService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventGetDto rejectEvent(@PathVariable @NotNull Long eventId) {
        log.debug("PATCH request for reject event by admin eventId={}", eventId);
        return eventService.rejectEventByAdmin(eventId);
    }
}
