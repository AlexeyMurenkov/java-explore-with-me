package ru.practicum.ewm.compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "compilations", schema = "public")
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    @Setter
    boolean pinned;
    @ManyToMany
    @JoinTable(name = "compilations_events", schema = "public", joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    Collection<Event> events;
}
