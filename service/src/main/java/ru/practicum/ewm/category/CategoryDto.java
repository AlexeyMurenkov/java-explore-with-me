package ru.practicum.ewm.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import ru.practicum.ewm.common.validation.group.Create;
import ru.practicum.ewm.common.validation.group.Update;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Value(staticConstructor = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    @Null(groups = Create.class, message = "id must be absent for this type of request")
    @NotNull(groups = Update.class, message = "id can't be empty for this type of request")
    Long id;

    @NotBlank(message = "Category name can't be blank")
    String name;
}
