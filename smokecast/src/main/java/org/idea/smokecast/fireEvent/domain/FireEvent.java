package org.idea.smokecast.fireEvent.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fire_events")
public class FireEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "centroid_lat", precision = 9, scale = 6)
    private BigDecimal centroidLat;

    @Column(name = "centroid_lon", precision = 9, scale = 6)
    private BigDecimal centroidLon;

    @Column(name = "max_frp", precision = 8, scale = 2)
    private BigDecimal maxFrp;

    @Column(name = "detection_count")
    private Integer detectionCount;

    @Column(name = "first_detected_at")
    private LocalDateTime firstDetectedAt;

    @Column(name = "last_detected_at")
    private LocalDateTime lastDetectedAt;

    @Column(name = "country_hint", length = 80)
    private String countryHint;
}