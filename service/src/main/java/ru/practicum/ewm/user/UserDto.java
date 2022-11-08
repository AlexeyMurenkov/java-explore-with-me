package ru.practicum.ewm.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Value(staticConstructor = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @Null
    Long id;
    @Email
    @NotBlank
    String email;
    @NotBlank
    String name;
}
