package org.idea.smokecast.fireEvent.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FireEventResponseDto {
    private Long id;
    private BigDecimal centroidLat;
    private BigDecimal centroidLon;
    private BigDecimal maxFrp;
    private Integer detectionCount;
    private LocalDateTime firstDetectedAt;
    private LocalDateTime lastDetectedAt;
    private String countryHint;
}