package org.idea.smokecast.fireDetection.application;

import org.idea.smokecast.fireDetection.domain.FireDetectionService;
import org.idea.smokecast.fireDetection.dto.FireDetectionResponseDto;
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
@RequestMapping("/api/v1/detection")
public class FireDetectionController {
    private final FireDetectionService fireDetectionService;

    @GetMapping
    public ResponseEntity<Page<FireDetectionResponseDto>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(fireDetectionService.getAllFireDetections(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FireDetectionResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fireDetectionService.getFireDetection(id));
    }
}