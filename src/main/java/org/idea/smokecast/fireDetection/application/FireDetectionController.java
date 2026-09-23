package org.idea.smokecast.fireDetection.application;

import org.idea.smokecast.fireDetection.domain.FireDetectionService;
import org.idea.smokecast.fireDetection.dto.FireDetectionResponseDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.idea.smokecast.fireDetection.dto.FireDetectionBulkPayload;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/detections")
public class FireDetectionController {
    private final FireDetectionService fireDetectionService;

    @GetMapping
    public ResponseEntity<Page<FireDetectionResponseDto>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        if (page < 0 || size < 1 || size > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page debe ser >= 0 y size entre 1 y 500");
        }
        return ResponseEntity.ok(fireDetectionService.getAllFireDetections(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FireDetectionResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fireDetectionService.getFireDetection(id));
    }

    @PostMapping("/bulk")
    public ResponseEntity<java.util.Map<String, Object>> insertBulk(@RequestBody FireDetectionBulkPayload payload) {
        try {
            var ids = fireDetectionService.insertBulk(payload == null ? null : payload.items());
            return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("inserted", ids.size(), "ids", ids));
        } catch (IllegalArgumentException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage(), error);
        }
    }
}
