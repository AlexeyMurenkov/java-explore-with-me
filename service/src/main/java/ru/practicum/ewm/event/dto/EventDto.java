package ru.practicum.ewm.event.dto;

import lombok.Value;
import ru.practicum.ewm.common.validation.annotation.FutureShift;
import ru.practicum.ewm.common.validation.annotation.NotBlankIfNotNull;
import ru.practicum.ewm.common.validation.group.Create;
import ru.practicum.ewm.common.validation.group.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value(staticConstructor = "of")
public class EventDto {

    @Null(groups = Create.class, message = "Event id must be empty")
    @NotNull(groups = Update.class, message = "Event id can't be empty")
    Long eventId;

    @NotBlank(groups = Create.class, message = "Event annotation can't be blank")
    @NotBlankIfNotNull(groups = Update.class, message = "Event annotation can't be blank")
    @Size(min = 20, max = 2000, message = "Event annotation length must be between 20 and 2000")
    String annotation;

    @NotNull(groups = Create.class, message = "Event category can't be empty")
    Long category;

    @NotBlank(groups = Create.class, message = "Event description can't be blank")
    @NotBlankIfNotNull(groups = Update.class, message = "Event description can't be blank")
    @Size(min = 20, max = 7000, message = "Event description length must be between 20 and 7000")
    String description;

    @NotNull(groups = Create.class, message = "Event date can't be empty")
    @FutureShift.List({
            @FutureShift(groups=Create.class, shiftHours = 1,
                    message = "The time of the event should be no earlier than an hour from the now"),
            @FutureShift(groups=Update.class, shiftHours = 2,
                    message = "The time of the event should be no earlier than 2 hours from the now")
    })
    LocalDateTime eventDate;

    @NotNull(groups = Create.class, message = "Event location can't be empty")
    Location location;

    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;

    @NotBlank(groups = Create.class, message = "Event title can't be blank")
    @NotBlankIfNotNull(groups = Update.class, message = "Event title can't be blank")
    @Size(min = 3, max = 120, message = "Event title length must be between 3 and 120")
    String title;
}
