package ru.practicum.ewm.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDto;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCompilationController {

    CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> getCompilations(@RequestParam Optional<Boolean> pinned,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable @NotNull Long compId) {
        return compilationService.getCompilation(compId);
    }
}
