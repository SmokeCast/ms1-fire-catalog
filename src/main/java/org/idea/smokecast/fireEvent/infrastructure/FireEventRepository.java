package org.idea.smokecast.fireEvent.infrastructure;

import org.idea.smokecast.fireEvent.domain.FireEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FireEventRepository extends JpaRepository<FireEvent, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<FireEvent> {
    @org.springframework.data.jpa.repository.Query("select distinct f.countryHint from FireEvent f where f.countryHint is not null order by f.countryHint")
    java.util.List<String> findCountries();
}