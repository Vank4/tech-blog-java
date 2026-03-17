package com.techblog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Application Properties
 * Ánh xạ các properties từ application.yml (prefix: app)
 */
@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {

    private Jwt jwt = new Jwt();
    private Cors cors = new Cors();
    private Upload upload = new Upload();

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private long expiration;
    }

    @Getter
    @Setter
    public static class Cors {
        private String[] allowedOrigins;
    }

    @Getter
    @Setter
    public static class Upload {
        private String path;
    }

}

