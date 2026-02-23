package com.techblog.config;

// TODO: Thêm dependency springdoc-openapi vào pom.xml
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.info.Info;
// import io.swagger.v3.oas.models.info.Contact;
// import io.swagger.v3.oas.models.security.SecurityScheme;
// import io.swagger.v3.oas.models.security.SecurityRequirement;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI Configuration
 * Cấu hình tài liệu API tự động với Swagger UI.
 *
 * Yêu cầu dependency:
 * <dependency>
 * <groupId>org.springdoc</groupId>
 * <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
 * <version>2.8.0</version>
 * </dependency>
 *
 * Swagger UI URL: http://localhost:8081/swagger-ui.html
 */
// @Configuration
public class SwaggerConfig {

    // TODO: Cấu hình OpenAPI
    // @Bean
    // public OpenAPI customOpenAPI() {
    // return new OpenAPI()
    // .info(new Info()
    // .title("Tech Blog API")
    // .version("1.0")
    // .description("API documentation for Tech Blog")
    // .contact(new Contact().name("Tech Blog Team")))
    // .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
    // .components(new io.swagger.v3.oas.models.Components()
    // .addSecuritySchemes("Bearer Authentication",
    // new SecurityScheme()
    // .type(SecurityScheme.Type.HTTP)
    // .bearerFormat("JWT")
    // .scheme("bearer")));
    // }

}
