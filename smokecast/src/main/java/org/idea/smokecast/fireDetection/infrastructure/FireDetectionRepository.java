package org.idea.smokecast.fireDetection.infrastructure;

import org.idea.smokecast.fireDetection.domain.FireDetection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FireDetectionRepository extends JpaRepository<FireDetection, Long> {
}