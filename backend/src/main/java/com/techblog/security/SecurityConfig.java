package com.techblog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// TODO: Thêm dependency spring-boot-starter-security vào pom.xml
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration
 * Cấu hình Spring Security: JWT filter, CORS, endpoint permissions.
 *
 * Yêu cầu dependency:
 * <dependency>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-starter-security</artifactId>
 * </dependency>
 */
// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
public class SecurityConfig {

    // TODO: Inject dependencies
    // private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // private final AuthEntryPoint authEntryPoint;
    // private final CustomUserDetailsService userDetailsService;

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // http
    // .csrf(csrf -> csrf.disable())
    // .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
    // .sessionManagement(session ->
    // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // .authorizeHttpRequests(auth -> auth
    // .requestMatchers("/api/auth/**", "/swagger-ui/**",
    // "/v3/api-docs/**").permitAll()
    // .requestMatchers("/api/admin/**").hasRole("ADMIN")
    // .anyRequest().authenticated()
    // )
    // .addFilterBefore(jwtAuthenticationFilter,
    // UsernamePasswordAuthenticationFilter.class);
    // return http.build();
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public AuthenticationManager
    // authenticationManager(AuthenticationConfiguration config) throws Exception {
    // return config.getAuthenticationManager();
    // }

}
