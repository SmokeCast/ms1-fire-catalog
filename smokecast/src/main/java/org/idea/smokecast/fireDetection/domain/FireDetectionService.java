package org.idea.smokecast.fireDetection.domain;

import java.util.NoSuchElementException;

import org.idea.smokecast.fireDetection.dto.FireDetectionResponseDto;
import org.idea.smokecast.fireDetection.infrastructure.FireDetectionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireDetectionService {
    private final FireDetectionRepository fireDetectionRepository;
    private final ModelMapper modelMapper;

    public Page<FireDetectionResponseDto> getAllFireDetections(PageRequest pageRequest) {
        return fireDetectionRepository.findAll(pageRequest)
                .map(fireEventResponseDto -> modelMapper.map(fireEventResponseDto, FireDetectionResponseDto.class));
    }

    public FireDetectionResponseDto getFireDetection(Long id) {
        var fireDetection = fireDetectionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Fire detection with ID not found: " + id));

        return modelMapper.map(fireDetection, FireDetectionResponseDto.class);
    }
}