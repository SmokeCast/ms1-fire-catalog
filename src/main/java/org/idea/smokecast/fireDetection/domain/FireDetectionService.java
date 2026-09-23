package org.idea.smokecast.fireDetection.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.idea.smokecast.fireDetection.dto.FireDetectionResponseDto;
import org.idea.smokecast.fireDetection.dto.FireDetectionBulkRequest;
import org.idea.smokecast.fireEvent.infrastructure.FireEventRepository;
import org.idea.smokecast.fireDetection.infrastructure.FireDetectionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireDetectionService {
    private final FireDetectionRepository fireDetectionRepository;
    private final FireEventRepository fireEventRepository;
    private final ModelMapper modelMapper;

    public Page<FireDetectionResponseDto> getAllFireDetections(PageRequest pageRequest) {
        return fireDetectionRepository.findAll(pageRequest)
                .map(fireEventResponseDto -> modelMapper.map(fireEventResponseDto, FireDetectionResponseDto.class));
    }

    public FireDetectionResponseDto getFireDetection(Long id) {
        var fireDetection = fireDetectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fire detection with ID not found: " + id));

        return modelMapper.map(fireDetection, FireDetectionResponseDto.class);
    }

    @Transactional
    public java.util.List<Long> insertBulk(java.util.List<FireDetectionBulkRequest> requests) {
        if (requests == null || requests.isEmpty() || requests.size() > 10000) {
            throw new IllegalArgumentException("El bulk debe contener entre 1 y 10000 elementos");
        }
        var detections = requests.stream().map(request -> {
            if (request == null || request.fireEventId() == null || request.latitude() == null || request.longitude() == null ||
                    request.frp() == null || request.acqDate() == null || request.acqTime() == null ||
                    request.acqTime().length() > 4 || (request.confidence() != null && request.confidence().length() > 10) ||
                    request.latitude().doubleValue() < -90 || request.latitude().doubleValue() > 90 ||
                    request.longitude().doubleValue() < -180 || request.longitude().doubleValue() > 180 ||
                    request.frp().signum() < 0) {
                throw new IllegalArgumentException("Datos inválidos para fire_detection");
            }
            var detection = new FireDetection();
            detection.setFireEvent(fireEventRepository.getReferenceById(request.fireEventId()));
            detection.setLatitude(request.latitude());
            detection.setLongitude(request.longitude());
            detection.setBrightness(request.brightness());
            detection.setFrp(request.frp());
            detection.setConfidence(request.confidence());
            detection.setAcqDate(request.acqDate());
            detection.setAcqTime(request.acqTime());
            return detection;
        }).toList();
        return fireDetectionRepository.saveAll(detections).stream().map(FireDetection::getId).toList();
    }
}
