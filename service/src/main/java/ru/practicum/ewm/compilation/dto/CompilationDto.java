package ru.practicum.ewm.compilation.dto;

import lombok.Value;
import ru.practicum.ewm.event.dto.EventGetDto;

import java.util.Collection;

@Value(staticConstructor = "of")
public class CompilationDto {
    Long id;
    String title;
    boolean pinned;
    Collection<EventGetDto> events;
}
