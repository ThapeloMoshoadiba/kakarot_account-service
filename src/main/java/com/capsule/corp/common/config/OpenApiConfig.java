package com.capsule.corp.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Account Service API",
            version = "${app.api.version}",
            description = "Provides a REST API for managing clients and their accounts",
            contact = @Contact(name = "Thapelo", email = "idk@capitecbank.co.za")))
// @SecurityScheme(
//        name = "Bearer Authentication (JWT)",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer")
public class OpenApiConfig {}
