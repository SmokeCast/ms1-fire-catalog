package org.idea.smokecast;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:h2:mem:smokecast;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SmokecastApplicationTests {
    @LocalServerPort int port;
    @Autowired JdbcTemplate jdbc;
    private final HttpClient client = HttpClient.newHttpClient();

    private HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path))
            .timeout(Duration.ofSeconds(5)).build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test void healthAndInvalidRequests() throws Exception {
        assertThat(get("/health").statusCode()).isEqualTo(200);
        assertThat(get("/api/v1/fires?size=0").statusCode()).isEqualTo(400);
        assertThat(get("/api/v1/detection?page=-1").statusCode()).isEqualTo(400);
        assertThat(get("/api/v1/fires/999999").statusCode()).isEqualTo(404);
        assertThat(get("/api/v1/detection/999999").statusCode()).isEqualTo(404);
    }

    @Test void readsEventsAndRelatedDetections() throws Exception {
        jdbc.update("INSERT INTO fire_events (id,centroid_lat,centroid_lon,detection_count) VALUES (1,-12,-77,1)");
        jdbc.update("INSERT INTO fire_detections (id,fire_event_id,latitude,longitude) VALUES (1,1,-12,-77)");
        try {
            var fires = get("/api/v1/fires");
            assertThat(fires.statusCode()).isEqualTo(200);
            assertThat(fires.body()).contains("\"content\"", "\"centroidLat\":-12");
            var detection = get("/api/v1/detection/1");
            assertThat(detection.statusCode()).isEqualTo(200);
            assertThat(detection.body()).contains("\"fireEvent\"", "\"latitude\":-12");
        } finally {
            jdbc.update("DELETE FROM fire_detections");
            jdbc.update("DELETE FROM fire_events");
        }
    }
}
