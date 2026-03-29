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
                        .requestMatchers("/", "/login", "/register", "/signup", "/profile", "/post/**", "/review/**").permitAll()
                        .requestMatchers("/author/**", "/admin/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**", "/favicon.ico").permitAll()

                        // ==========================================
                        // 1. PUBLIC (ĐỘC GIẢ & TÀI NGUYÊN TĨNH)
                        // ==========================================
                        .requestMatchers("/", "/index.html", "/favicon.ico", "/static/**", "/css/**", "/js/**",
                                "/images/**")
                        .permitAll()
                        .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()

                        // ==========================================
                        // 2. AUTHOR (TÁC GIẢ - WORKFLOW VIẾT BÀI)
                        // ==========================================
                        // Giao diện soạn thảo & danh sách bài cá nhân

                        // API nghiệp vụ của Tác giả
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/posts/**").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/**").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/posts/*/submit").hasRole("AUTHOR")

                        // ==========================================
                        // 3. REVIEWS (ĐÁNH GIÁ SẢN PHẨM)
                        // ==========================================
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/latest", "/api/v1/reviews/slug/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/{id:[0-9]+}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/reviews/**").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/reviews/*/submit").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers("/api/v1/reviews/mine").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers("/api/v1/reviews/pending").hasRole("ADMIN")
                        .requestMatchers("/api/v1/reviews/*/approve").hasRole("ADMIN")
                        .requestMatchers("/api/v1/reviews/*/reject").hasRole("ADMIN")
                        .requestMatchers("/api/v1/reviews/*/hide").hasRole("ADMIN")

                        // API Upload ảnh cho bài viết
                        .requestMatchers("/api/v1/files/upload").authenticated()

                        // ==========================================
                        // 4. COMMENTS & REPORTING
                        // ==========================================
                        .requestMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/comments/**").authenticated()
                        .requestMatchers("/api/v1/admin/comments/**").hasRole("ADMIN")

                        // ==========================================
                        // 5. USER INTERACTIONS (LIKES & SAVES)
                        // ==========================================
                        .requestMatchers(HttpMethod.GET, "/api/v1/interactions/status").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/interactions/**").authenticated()

                        // ==========================================
                        // 3. ADMIN (QUẢN TRỊ VIÊN - KIỂM DUYỆT & HỆ THỐNG)
                        // ==========================================
                        // Giao diện quản trị tổng thể

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
                        .anyRequest().authenticated())
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
