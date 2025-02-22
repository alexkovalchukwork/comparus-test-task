package com.comparus.service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "User Aggregation Service API",
        version = "1.0",
        description = "API for aggregating user data from multiple databases"
    )
)
public class SwaggerConfig {
}