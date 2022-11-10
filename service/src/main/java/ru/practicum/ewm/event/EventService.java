package ru.practicum.ewm.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.common.FromIndexPageRequest;
import ru.practicum.ewm.common.StatsClient;
import ru.practicum.ewm.common.exception.ForbiddenException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.RequestRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {

    private static final LocalDateTime MIN_DATE_TIME = LocalDateTime.of(2000, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE_TIME = LocalDateTime.of(2099, 12, 31, 23, 59);

    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    RequestRepository requestRepository;
    StatsClient statsClient;

    private Category findCategory(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(String.format("Category id=%s not found", categoryId))
        );
    }

    private User findUser(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User id=%s not found", userId))
        );
    }

    private Event findEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event id=%s not found", eventId))
        );
    }

    private Request findRequest(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(String.format("Request id=%s not found", requestId))
        );
    }

    private Event findUserEvent(long userId, long eventId) {
        final User user = findUser(userId);
        final Event event = findEvent(eventId);

        if (!user.getId().equals(userId)) {
            throw new ForbiddenException(String.format("User id=%s is not initiator of event id=%s", userId, eventId));
        }

        return event;
    }

    private Event patchEvent(Event origin, EventDto patch) {
        if (patch.getCategory() != null) origin.setCategory(findCategory(patch.getCategory()));
        if (patch.getAnnotation() != null) origin.setAnnotation(patch.getAnnotation());
        if (patch.getDescription() != null) origin.setDescription(patch.getDescription());
        if (patch.getEventDate() != null) origin.setEventDate(patch.getEventDate());
        if (patch.getPaid() != null) origin.setPaid(patch.getPaid());
        if (patch.getParticipantLimit() != null) origin.setParticipantLimit(patch.getParticipantLimit());
        if (patch.getTitle() != null) origin.setTitle(patch.getTitle());
        if (patch.getLocation() != null) {
            origin.setLat(patch.getLocation().getLat());
            origin.setLon(patch.getLocation().getLon());
        }

        return origin;
    }

    private int getViews(Event event) {
        final String eventUri = "/event/" + event.getId();
        return statsClient.get(event.getCreatedOn(), LocalDateTime.now(), List.of(eventUri)).length;
    }

    public EventGetDto createEvent(long userId, EventDto eventDto) {
        final User user = findUser(userId);

        final Event event = EventMapper.fromEventDto(eventDto, user, null);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event), 0, 0);
    }

    public EventGetDto updateEvent(long userId, EventDto eventDto) {
        final Event event = findUserEvent(userId, eventDto.getEventId());

        switch (event.getState()) {
            case PUBLISHED:
                throw new ForbiddenException(String.format("Can't update event in state '%s'",
                        event.getState().toString()));
            case CANCELED:
                event.setState(EventState.PENDING);
        }

        final Event patchedEvent = patchEvent(event, eventDto);

        final Event updatedEvent = eventRepository.save(patchedEvent);

        final int confirmedRequest = requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);
        return EventMapper.toEventFullDto(updatedEvent, confirmedRequest, getViews(event));
    }

    public Collection<EventGetDto> getEventsByUserId(long userId, int from, int size) {
        final User user = findUser(userId);
        final Pageable pageable = FromIndexPageRequest.of(from, size);
        final Collection<Event> events = eventRepository.findAllByInitiator(user, pageable);
        return events.stream()
                .map((e) -> EventMapper.toEventShortDto(e, requestRepository.countByEventAndStatus(e,
                        RequestStatus.CONFIRMED), getViews(e))).collect(Collectors.toList());
    }

    public EventGetDto getEventByIdAndUserId(long eventId, long userId) {
        final Event event = findUserEvent(userId, eventId);
        final int confirmedRequests = requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);
        return EventMapper.toEventFullDto(event, confirmedRequests, getViews(event));
    }

    public EventGetDto cancelEventByIdAndUserId(long eventId, long userId) {
        final Event event = findUserEvent(userId, eventId);
        event.setState(EventState.CANCELED);
        final int confirmedRequests = requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);
        return EventMapper.toEventFullDto(eventRepository.save(event), confirmedRequests, getViews(event));
    }

    public Collection<RequestDto> getRequestsByUserIdAndEventId(long userId, long eventId) {
        final Event event = findUserEvent(userId, eventId);
        final Collection<Request> requests = requestRepository.findAllByEvent(event);
        return RequestMapper.toRequestDtos(requests);
    }

    public RequestDto confirmRequest(long userId, long eventId, long reqId) {
        final Event event = findUserEvent(userId, eventId);
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new ForbiddenException(
                    "Event for confirm request should be participant limited and moderation requested"
            );
        }
        final int reqCount = requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);
        if (reqCount >= event.getParticipantLimit()) {
            throw new ForbiddenException(
                    "For this event already reach the participant limit"
            );
        }
        final Request request = findRequest(reqId);
        if (!request.getEvent().getId().equals(eventId)) {
            throw new HttpMessageConversionException("Request do not have for event");
        }
        request.setStatus(RequestStatus.CONFIRMED);
        final Request confirmedReq = requestRepository.save(request);
        if (reqCount >= event.getParticipantLimit()) {
            requestRepository.findAllByEventAndStatus(event, RequestStatus.PENDING)
                    .forEach((r) -> {
                        r.setStatus(RequestStatus.REJECTED);
                        requestRepository.save(r);
                    });
        }
        return RequestMapper.toRequestDto(confirmedReq);
    }

    public RequestDto rejectRequest(long userId, long eventId, long reqId) {
        findUserEvent(userId, eventId);
        final Request request = findRequest(reqId);
        if (!request.getEvent().getId().equals(eventId)) {
            throw new HttpMessageConversionException("Request do not have for event");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ForbiddenException("Raquest may rejected in pending status only");
        }
        request.setStatus(RequestStatus.REJECTED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    public Collection<RequestDto> getRequests(long userId) {
        final User user = findUser(userId);
        return RequestMapper.toRequestDtos(requestRepository.findAllByRequester(user));
    }

    public RequestDto createRequest(long userId, long eventId) {
        final User user = findUser(userId);
        final Event event = findEvent(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException(String.format("Requested event id=%s is not published", eventId));
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException(String.format("User id=%s is initiator of event id=%s", userId, eventId));
        }

        final Request request = Request.of(user, event);
        request.setStatus(event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    public RequestDto cancelRequest(long userId, long reqId) {
        findUser(userId);
        final Request req = findRequest(reqId);
        if (!req.getRequester().getId().equals(userId)) {
            throw new ForbiddenException("User may cancel own request only");
        }
        req.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(req));
    }

    public Collection<EventGetDto> getEventsByAdmin(Long[] userIds, EventState[] states, Long[] catIds,
                                                    Optional<LocalDateTime> rangeStart,
                                                    Optional<LocalDateTime> rangeEnd, int from, int size) {

        final Pageable pageable = FromIndexPageRequest.of(from, size);

        BooleanBuilder filter = new BooleanBuilder(QEvent.event.eventDate.after(rangeStart.orElse(MIN_DATE_TIME))
                        .and(QEvent.event.eventDate.before(rangeEnd.orElse(MAX_DATE_TIME)))
        );
        if (userIds != null && userIds.length > 0) {
            filter.and(QEvent.event.initiator.id.in(userIds));
        }
        if (states != null && states.length > 0) {
            filter.and(QEvent.event.state.in(states));
        }
        if (catIds != null && catIds.length > 0) {
            filter.and(QEvent.event.category.id.in(catIds));
        }

        return eventRepository.findAll(filter, pageable).toList().stream()
                .map((e) -> EventMapper.toEventFullDto(e, requestRepository.countByEventAndStatus(e,
                        RequestStatus.CONFIRMED), getViews(e))).collect(Collectors.toList());
    }

    public EventGetDto updateEventByAdmin(long eventId, EventDto eventDto) {
        final Event event = findEvent(eventId);
        final Event updatedEvent = patchEvent(event, eventDto);

        final int confirmedRequests = requestRepository.countByEventAndStatus(updatedEvent, RequestStatus.CONFIRMED);
        return EventMapper.toEventFullDto(eventRepository.save(updatedEvent), confirmedRequests, getViews(event));
    }

    public EventGetDto publishEventByAdmin(long eventId) {
        final Event event = findEvent(eventId);
        if (event.getState() == EventState.CANCELED) {
            throw new ForbiddenException("Canceled event do not by published");
        }
        if (event.getEventDate().minusHours(1).isBefore(LocalDateTime.now())) {
            throw new ForbiddenException(
                    "The time of the publish event should be no earlier than an hour from the now"
            );
        }
        event.setState(EventState.PUBLISHED);
        final int confirmedRequests = requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);
        return EventMapper.toEventFullDto(eventRepository.save(event), confirmedRequests, getViews(event));
    }

    public EventGetDto rejectEventByAdmin(long eventId) {
        final Event event = findEvent(eventId);
        if (event.getState() != EventState.PENDING) {
            throw new ForbiddenException("Event for reject should be in pending state");
        }
        event.setState(EventState.CANCELED);
        final int confirmedRequests = requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);
        return EventMapper.toEventFullDto(eventRepository.save(event), confirmedRequests, getViews(event));
    }

    public Collection<EventGetDto> getEventsByPublic(Optional<String> text, Long[] categories, Optional<Boolean> paid,
                                       Optional<LocalDateTime> rangeStart, Optional<LocalDateTime> rangeEnd,
                                       boolean onlyAvailable, Optional<EventSort> sort, int from, int size) {

        BooleanBuilder filter = new BooleanBuilder(QEvent.event.eventDate.after(rangeStart.orElse(MIN_DATE_TIME))
                .and(QEvent.event.eventDate.before(rangeEnd.orElse(MAX_DATE_TIME)))
        );

        if (text.isPresent()) {
            final BooleanExpression textFilter = QEvent.event.annotation.likeIgnoreCase(text.get())
                    .or(QEvent.event.description.likeIgnoreCase(text.get()));
            filter.and(textFilter);
        }
        if (categories.length > 0) {
            filter.and(QEvent.event.category.id.in(categories));
        }
        paid.ifPresent(aBoolean -> filter.and(QEvent.event.paid.eq(aBoolean)));

        final Stream<Event> events;
        if (sort.isPresent() && sort.get() == EventSort.EVENT_DATE) {
            events = StreamSupport.stream(eventRepository.findAll(filter, Sort.by("eventDate")).spliterator(),
                    false);
        } else {
            events = StreamSupport.stream(eventRepository.findAll(filter).spliterator(), false);
        }

        final Stream<EventGetDto> eventDtos = events
                .filter(
                        (e) -> !onlyAvailable || e.getParticipantLimit() == 0L ||
                                e.getParticipantLimit() >= requestRepository.countByEventAndStatus(e,
                                        RequestStatus.CONFIRMED)
                )
                .map(
                        (e) -> EventMapper.toEventFullDto(e, requestRepository.countByEventAndStatus(e,
                                RequestStatus.CONFIRMED), getViews(e))
                );

        if (sort.isPresent() && sort.get() == EventSort.EVENT_DATE) {
            return eventDtos
                    .sorted(Comparator.comparing(EventGetDto::getViews))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
        return eventDtos
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public EventGetDto getEventByPublic(long eventId) {
        final Event event = findEvent(eventId);
        return EventMapper.toEventFullDto(event, requestRepository.countByEventAndStatus(event,
                RequestStatus.CONFIRMED), getViews(event));
    }
}
