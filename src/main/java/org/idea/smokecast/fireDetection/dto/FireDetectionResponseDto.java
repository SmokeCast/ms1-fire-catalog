package org.idea.smokecast.fireDetection.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.idea.smokecast.fireEvent.dto.FireEventSummaryDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FireDetectionResponseDto {
    private Long id;
    private FireEventSummaryDto fireEvent;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal brightness;
    private BigDecimal frp;
    private String confidence;
    private LocalDate acqDate;
    private String acqTime;
}