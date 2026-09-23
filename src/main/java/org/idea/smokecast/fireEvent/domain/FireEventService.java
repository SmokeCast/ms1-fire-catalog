package org.idea.smokecast.fireEvent.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.idea.smokecast.fireEvent.dto.FireEventResponseDto;
import org.idea.smokecast.fireEvent.dto.FireEventBulkRequest;
import org.idea.smokecast.fireEvent.infrastructure.FireEventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireEventService {
    private final FireEventRepository fireEventRepository;
    private final ModelMapper modelMapper;

    public java.util.List<String> getCountries() {
        return fireEventRepository.findCountries();
    }

    public Page<FireEventResponseDto> getAllFireEvents(PageRequest pageRequest, String country, String severity, String q) {
        org.springframework.data.jpa.domain.Specification<FireEvent> filter = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (!country.isBlank()) predicates.add(cb.equal(cb.lower(root.get("countryHint")), country.toLowerCase(java.util.Locale.ROOT)));
            var frp = root.<java.math.BigDecimal>get("maxFrp");
            switch (severity) {
                case "Bajo" -> predicates.add(cb.le(cb.coalesce(frp, java.math.BigDecimal.ZERO), 30));
                case "Moderado" -> predicates.add(cb.and(cb.gt(frp, 30), cb.le(frp, 90)));
                case "Alto" -> predicates.add(cb.and(cb.gt(frp, 90), cb.le(frp, 150)));
                case "Crítico" -> predicates.add(cb.gt(frp, 150));
                case "" -> { }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Intensidad inválida");
            }
            if (!q.isBlank()) {
                String term = q.toLowerCase(java.util.Locale.ROOT);
                String escaped = term.replace("!", "!!").replace("%", "!%").replace("_", "!_");
                var match = cb.like(cb.lower(root.get("countryHint")), "%" + escaped + "%", '!');
                String numeric = term.replaceFirst("^incendio\\s*#?\\s*", "").replaceFirst("^#", "");
                try {
                    match = cb.or(match, cb.equal(root.get("id"), Long.parseLong(numeric)));
                } catch (NumberFormatException ignored) { }
                predicates.add(match);
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return fireEventRepository.findAll(filter, pageRequest)
                .map(event -> modelMapper.map(event, FireEventResponseDto.class));
    }

    public FireEventResponseDto getFireEvent(Long id) {
        var fireEvent = fireEventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fire event with ID not found: " + id));

        return modelMapper.map(fireEvent, FireEventResponseDto.class);
    }

    @Transactional
    public java.util.List<Long> insertBulk(java.util.List<FireEventBulkRequest> requests) {
        validateBulk(requests);
        var events = requests.stream().map(request -> {
            validate(request);
            var event = new FireEvent();
            event.setCentroidLat(request.centroidLat());
            event.setCentroidLon(request.centroidLon());
            event.setMaxFrp(request.maxFrp());
            event.setDetectionCount(request.detectionCount());
            event.setFirstDetectedAt(request.firstDetectedAt());
            event.setLastDetectedAt(request.lastDetectedAt());
            event.setCountryHint(request.countryHint());
            return event;
        }).toList();
        return fireEventRepository.saveAll(events).stream().map(FireEvent::getId).toList();
    }

    private void validateBulk(java.util.List<?> requests) {
        if (requests == null || requests.isEmpty() || requests.size() > 10000) {
            throw new IllegalArgumentException("El bulk debe contener entre 1 y 10000 elementos");
        }
    }

    private void validate(FireEventBulkRequest request) {
        if (request == null || request.centroidLat() == null || request.centroidLon() == null ||
                request.maxFrp() == null || request.detectionCount() == null || request.firstDetectedAt() == null ||
                request.lastDetectedAt() == null || request.countryHint() == null || request.countryHint().isBlank() ||
                request.countryHint().length() > 80 ||
                request.centroidLat().doubleValue() < -90 || request.centroidLat().doubleValue() > 90 ||
                request.centroidLon().doubleValue() < -180 || request.centroidLon().doubleValue() > 180 ||
                request.maxFrp().signum() < 0 || request.detectionCount() < 0) {
            throw new IllegalArgumentException("Datos inválidos para fire_event");
        }
    }
}
