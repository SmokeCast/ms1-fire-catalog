package org.idea.smokecast;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {
    private final JdbcTemplate jdbc;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        try {
            jdbc.queryForObject("SELECT 1", Integer.class);
            return ResponseEntity.ok(Map.of("status", "ok", "service", "ms1-fire-catalog"));
        } catch (org.springframework.dao.DataAccessException exception) {
            return ResponseEntity.status(503).body(Map.of("status", "degraded", "service", "ms1-fire-catalog"));
        }
    }
}
