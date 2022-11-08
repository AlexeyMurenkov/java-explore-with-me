package ru.practicum.ewm.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = { @UniqueConstraint(columnNames = {"requester_id", "event_id"}) },
        name = "requests", schema = "public"
)
@Getter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    @NonNull
    User requester;
    @ManyToOne
    @JoinColumn(name = "event_id")
    @NonNull
    Event event;
    @Setter
    RequestStatus status = RequestStatus.PENDING;
    LocalDateTime created = LocalDateTime.now();
}
