package ru.practicum.ewm.compilation.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Value
public class NewCompilationDto {
    @NotBlank
    String title;
    boolean pinned;
    Set<Long> events;
}
