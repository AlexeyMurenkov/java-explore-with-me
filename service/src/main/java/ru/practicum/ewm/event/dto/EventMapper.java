package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventMapper {
    public static Event fromEventDto(EventDto eventDto, User initiator, Long eventId) {
        return Event.builder()
                .id(eventId != null ? eventId : eventDto.getEventId())
                .initiator(initiator)
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .category(Category.of(eventDto.getCategory(), ""))
                .eventDate(eventDto.getEventDate())
                .lat(eventDto.getLocation() != null ? eventDto.getLocation().getLat() : 0)
                .lon(eventDto.getLocation() != null ? eventDto.getLocation().getLon() : 0)
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .title(eventDto.getTitle())
                .requestModeration(Optional.ofNullable(eventDto.getRequestModeration()).orElse(true))
                .build();
    }

    public static EventGetDto toEventFullDto(Event event, Integer confirmedRequest, int views) {
        return EventGetDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequest)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .location(Location.of(event.getLat(), event.getLon()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventGetDto toEventShortDto(Event event, Integer confirmedRequest, int views) {
        return EventGetDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequest)
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static Collection<EventGetDto> toEventShortDtos(Collection<Event> events) {
        return events.stream()
                .map((e) -> toEventShortDto(e, null, 0))
                .collect(Collectors.toList());
    }
}
