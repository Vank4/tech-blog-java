package com.techblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.techblog.domain")
@EnableTransactionManagement
@EnableJpaAuditing
public class JpaConfig {

    // Spring Boot auto-configures DataSource and EntityManager from application.yml.
}
