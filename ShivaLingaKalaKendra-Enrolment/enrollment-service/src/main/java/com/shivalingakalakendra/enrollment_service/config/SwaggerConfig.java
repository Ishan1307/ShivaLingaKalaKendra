package com.shivalingakalakendra.enrollment_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Enrolment Service API",
        version = "v1",
        description = "API documentation for Enrolment Service"
))
public class SwaggerConfig {
}