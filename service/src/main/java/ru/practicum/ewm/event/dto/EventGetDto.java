package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.dto.Location;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.UserDto;

import java.time.LocalDateTime;

@Value(staticConstructor = "of")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventGetDto {
    Long id;
    String annotation;
    CategoryDto category;
    Integer confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    UserDto initiator;
    @With
    Location location;
    Boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    EventState state;
    String title;
    Integer views;
}
