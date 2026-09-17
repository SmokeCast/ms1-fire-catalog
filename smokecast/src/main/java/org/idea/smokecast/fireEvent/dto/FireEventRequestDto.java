package org.idea.smokecast.fireEvent.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FireEventRequestDto {
    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    @Digits(integer = 3, fraction = 6)
    private BigDecimal centroidLat;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @Digits(integer = 3, fraction = 6)
    private BigDecimal centroidLon;

    @DecimalMin(value = "0.0")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal maxFrp;

    @Min(value = 1)
    private Integer detectionCount;

    @NotNull
    @PastOrPresent
    private LocalDateTime firstDetectedAt;

    @PastOrPresent
    private LocalDateTime lastDetectedAt;

    @Size(max = 80)
    private String countryHint;
}