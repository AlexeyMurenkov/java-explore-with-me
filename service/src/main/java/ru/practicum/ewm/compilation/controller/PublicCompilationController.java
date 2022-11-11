package ru.practicum.ewm.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDto;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@Slf4j
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
        log.debug("GET request for compilations by pinned filter from={} size={}", from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable @NotNull Long compId) {
        log.debug("GET request for compilation id={}", compId);
        return compilationService.getCompilation(compId);
    }
}
