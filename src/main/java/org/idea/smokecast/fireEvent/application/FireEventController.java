package org.idea.smokecast.fireEvent.application;

import org.idea.smokecast.fireEvent.domain.FireEventService;
import org.idea.smokecast.fireEvent.dto.FireEventResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fires")
public class FireEventController {
    private final FireEventService fireEventService;

    @GetMapping
    public ResponseEntity<Page<FireEventResponseDto>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String country,
            @RequestParam(defaultValue = "") String severity,
            @RequestParam(defaultValue = "") String q) {
        if (page < 0 || size < 1 || size > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page debe ser >= 0 y size entre 1 y 500");
        }
        if (country.length() > 80 || q.length() > 100 || !java.util.List.of("", "Bajo", "Moderado", "Alto", "Crítico").contains(severity)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filtros inválidos");
        }
        return ResponseEntity.ok(fireEventService.getAllFireEvents(
            PageRequest.of(page, size, org.springframework.data.domain.Sort.by("id")), country.trim(), severity, q.trim()));
    }

    @GetMapping("/countries")
    public java.util.List<String> countries() {
        return fireEventService.getCountries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FireEventResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fireEventService.getFireEvent(id));
    }
}