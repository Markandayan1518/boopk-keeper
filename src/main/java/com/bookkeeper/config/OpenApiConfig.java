package com.bookkeeper.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookKeeperOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Keeper API")
                        .version("0.1.0")
                        .description("Auto-generated API documentation for the Book Keeper application."));
    }
}
