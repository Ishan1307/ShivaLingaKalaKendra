package com.shivalingakalakendra.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Shivalinga Kala Kendra API",
        version = "v1",
        description = "API documentation for Shivalinga Kala Kendra"
))
public class OpenApiConfig {
}