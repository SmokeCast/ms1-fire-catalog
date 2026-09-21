package org.idea.smokecast.fireEvent.infrastructure;

import org.idea.smokecast.fireEvent.domain.FireEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FireEventRepository extends JpaRepository<FireEvent, Long> {
}