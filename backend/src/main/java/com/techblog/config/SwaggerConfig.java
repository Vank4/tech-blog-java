package com.techblog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI Configuration
 * Cấu hình tài liệu API tự động với Swagger UI.
 *
 * Swagger UI URL: http://localhost:8081/swagger-ui.html
 * API Docs: http://localhost:8081/v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tech Blog Platform API")
                        .version("1.0.0")
                        .description("RESTful API cho nền tảng Blog công nghệ & Review sản phẩm AI-powered")
                        .contact(new Contact()
                                .name("Tech Blog Team")
                                .email("support@techblog.vn")
                                .url("https://techblog.vn")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập JWT token từ /api/v1/auth/login")));
    }

}

