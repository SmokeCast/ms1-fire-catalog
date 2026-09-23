package org.idea.smokecast.fireDetection.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FireDetectionBulkRequest(
        Long fireEventId,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal brightness,
        BigDecimal frp,
        String confidence,
        LocalDate acqDate,
        String acqTime) {
}
