package ru.practicum.ewm.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(length = 2000)
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @Column(length = 7000)
    String description;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    Float lat;
    Float lon;
    boolean paid;
    int participantLimit;
    @Column(name = "request_moderation")
    boolean requestModeration;
    @Column(length = 120)
    String title;
    EventState state;
    @Column(name = "published_on")
    LocalDateTime publishedOn;

    public void setState(EventState state) {
        if (state == EventState.PUBLISHED && this.state != EventState.PUBLISHED) {
            publishedOn = LocalDateTime.now();
        }
        this.state = state;
    }
}
