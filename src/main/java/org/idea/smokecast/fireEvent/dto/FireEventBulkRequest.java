package org.idea.smokecast.fireEvent.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FireEventBulkRequest(
        BigDecimal centroidLat,
        BigDecimal centroidLon,
        BigDecimal maxFrp,
        Integer detectionCount,
        LocalDateTime firstDetectedAt,
        LocalDateTime lastDetectedAt,
        String countryHint) {
}
