package org.idea.smokecast.fireEvent.application;

import org.idea.smokecast.fireEvent.domain.FireEventService;
import org.idea.smokecast.fireEvent.dto.FireEventResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(fireEventService.getAllFireEvents(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FireEventResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fireEventService.getFireEvent(id));
    }
}