package org.idea.smokecast.fireDetection.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FireDetectionRequestDto {
    @NotNull
    private Long fireEventId;

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    @Digits(integer = 3, fraction = 6)
    private BigDecimal latitude;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @Digits(integer = 3, fraction = 6)
    private BigDecimal longitude;

    @DecimalMin(value = "0.0")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal brightness;

    @DecimalMin(value = "0.0")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal frp;

    @Size(max = 10)
    private String confidence;

    @NotNull
    private LocalDate acqDate;

    @Size(min = 4, max = 4)
    private String acqTime;
}