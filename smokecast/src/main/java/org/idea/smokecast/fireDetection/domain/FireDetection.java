package org.idea.smokecast.fireDetection.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.idea.smokecast.fireEvent.domain.FireEvent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "fire_detections")
public class FireDetection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fire_event_id", nullable = false)
    private FireEvent fireEvent;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "brightness", precision = 8, scale = 2)
    private BigDecimal brightness;

    @Column(name = "frp", precision = 8, scale = 2)
    private BigDecimal frp;

    @Column(name = "confidence", length = 10)
    private String confidence;

    @Column(name = "acq_date")
    private LocalDate acqDate;

    @Column(name = "acq_time", length = 4)
    private String acqTime;
}