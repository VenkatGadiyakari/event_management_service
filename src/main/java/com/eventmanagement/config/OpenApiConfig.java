package com.eventmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Event Management Service")
                        .description("Manages events, ticket tiers, and venues. " +
                                "Admin endpoints require X-User-Id header (organiser's UUID from auth-service). " +
                                "Public endpoints are open.")
                        .version("1.0.0"));
    }

    @Bean
    public OperationCustomizer adminHeaderCustomizer() {
        return (operation, handlerMethod) -> {
            if (handlerMethod.getBeanType().getSimpleName().equals("AdminEventController")) {
                operation.addParametersItem(new Parameter()
                        .in("header")
                        .name("X-User-Id")
                        .description("Organiser's UUID (injected by API gateway after JWT validation)")
                        .required(true)
                        .schema(new io.swagger.v3.oas.models.media.StringSchema()
                                .format("uuid")
                                .example("a1b2c3d4-e5f6-7890-abcd-ef1234567890")));
            }
            return operation;
        };
    }
}
