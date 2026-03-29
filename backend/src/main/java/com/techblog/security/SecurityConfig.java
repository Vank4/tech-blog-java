package com.techblog.security;

import com.techblog.security.filter.JwtAuthenticationFilter;
import com.techblog.security.handler.AuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ==========================================
                        // 1. PUBLIC (ĐỘC GIẢ & TÀI NGUYÊN TĨNH)
                        // ==========================================
                        .requestMatchers("/", "/index.html", "/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll() // Xem ảnh đại diện bài viết

                        // Cho phép xem nội dung nhưng không được sửa
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()

                        // ==========================================
                        // 2. AUTHOR (TÁC GIẢ - WORKFLOW VIẾT BÀI)
                        // ==========================================
                        // Giao diện soạn thảo & danh sách bài cá nhân
                        .requestMatchers("/author/**").hasAnyRole("AUTHOR", "ADMIN")

                        // API nghiệp vụ của Tác giả
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/posts/**").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/**").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/posts/*/submit").hasRole("AUTHOR")

                        // API Upload ảnh cho bài viết
                        .requestMatchers("/api/v1/files/upload").hasAnyRole("AUTHOR", "ADMIN")

                        // ==========================================
                        // 3. ADMIN (QUẢN TRỊ VIÊN - KIỂM DUYỆT & HỆ THỐNG)
                        // ==========================================
                        // Giao diện quản trị tổng thể
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Quản lý Danh mục & Thẻ (Chỉ Admin mới có quyền CRUD)
                        .requestMatchers("/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/tags/**").hasRole("ADMIN")

                        // Nghiệp vụ kiểm duyệt (Approve/Reject/Hide/Featured/Logs)
                        .requestMatchers("/api/v1/posts/*/approve").hasRole("ADMIN")
                        .requestMatchers("/api/v1/posts/*/reject").hasRole("ADMIN")
                        .requestMatchers("/api/v1/posts/*/hide").hasRole("ADMIN")
                        .requestMatchers("/api/v1/posts/*/featured").hasRole("ADMIN")
                        .requestMatchers("/api/v1/posts/moderation-logs").hasRole("ADMIN")

                        // ==========================================
                        // 4. CHỐT CHẶN CUỐI CÙNG
                        // ==========================================
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}