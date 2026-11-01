package com.shivalingakalakendra.identity_service.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Identity Service API",
                version = "v1",
                description = "API documentation for Identity Service"
        )
)
public class SwaggerConfig {
}
