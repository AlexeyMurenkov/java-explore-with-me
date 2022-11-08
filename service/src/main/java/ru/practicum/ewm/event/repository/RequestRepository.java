package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Request;
import ru.practicum.ewm.event.model.RequestStatus;
import ru.practicum.ewm.user.User;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findAllByEvent(Event event);
    int countByEventAndStatus(Event event, RequestStatus status);
    Collection<Request> findAllByEventAndStatus(Event event, RequestStatus status);
    Collection<Request> findAllByRequester(User requester);
}
