package com.techblog.config;

// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.stereotype.Component;

/**
 * Application Properties
 * Đọc cấu hình custom từ application.yml (prefix: app)
 *
 * Ví dụ trong application.yml:
 * app:
 * jwt:
 * secret: your-secret-key
 * expiration: 86400000
 * upload:
 * max-size: 10MB
 * allowed-types: jpg,png,gif,webp
 * cors:
 * allowed-origins: http://localhost:3000
 */
// @Component
// @ConfigurationProperties(prefix = "app")
public class AppProperties {

    // TODO: Định nghĩa các thuộc tính cấu hình custom
    // private JwtProperties jwt = new JwtProperties();
    // private UploadProperties upload = new UploadProperties();

    // public static class JwtProperties {
    // private String secret;
    // private long expiration = 86400000; // 24 hours
    // // getters & setters
    // }

    // public static class UploadProperties {
    // private String maxSize = "10MB";
    // private String allowedTypes = "jpg,png,gif,webp";
    // // getters & setters
    // }

}
