package ru.practicum.ewm.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value(staticConstructor = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    String message;
    String reason;
    String status;
    LocalDateTime timestamp = LocalDateTime.now();
    List<String> errors;
}
