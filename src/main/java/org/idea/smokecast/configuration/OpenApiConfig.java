package org.idea.smokecast.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI smokeCastOpenAPI(@Value("${PUBLIC_BASE_PATH:/}") String configuredPath) {
        var path = configuredPath == null ? "/" : configuredPath.trim();
        if (path.isBlank()) path = "/";
        if (!path.startsWith("/")) path = "/" + path;
        if (path.length() > 1) path = path.replaceAll("/+$", "");

        return new OpenAPI()
                .info(new Info()
                        .title("MS1 — Fire Catalog API")
                        .version("1.0.0"))
                .servers(List.of(new Server().url(path)));
    }
}
