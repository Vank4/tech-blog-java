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
                        // 1. PUBLIC (GIAO DIỆN UI & TÀI NGUYÊN TĨNH)
                        // ==========================================
                        .requestMatchers("/", "/index.html", "/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                        .requestMatchers(
                                "/login", "/register", "/signup", "/forgot-password", "/reset-password",
                                "/profile", "/products", "/products/**", "/compare", "/post/**", "/review/**",
                                "/author/**", "/admin/**"
                        ).permitAll()
                        .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // ==========================================
                        // 2. PUBLIC API DATA (KHÔNG CẦN ĐĂNG NHẬP ĐỂ XEM)
                        // ==========================================
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/latest", "/api/v1/reviews/slug/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/{id:[0-9]+}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/interactions/status").permitAll()

                        // ==========================================
                        // 3. ADMIN (API QUẢN TRỊ VIÊN - ƯU TIÊN KIỂM TRA TRƯỚC)
                        // ==========================================
                        .requestMatchers("/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/tags/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/products/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/comments/**").hasRole("ADMIN")

                        // Admin duyệt/ẩn Bài Viết
                        .requestMatchers("/api/v1/posts/*/approve", "/api/v1/posts/*/reject", "/api/v1/posts/*/hide", "/api/v1/posts/*/featured").hasRole("ADMIN")
                        .requestMatchers("/api/v1/posts/moderation-logs").hasRole("ADMIN")

                        // Admin duyệt/ẩn Reviews
                        .requestMatchers("/api/v1/reviews/pending", "/api/v1/reviews/*/approve", "/api/v1/reviews/*/reject", "/api/v1/reviews/*/hide").hasRole("ADMIN")

                        // ==========================================
                        // 4. AUTHOR / AUTHENTICATED (API NGHIỆP VỤ CHUNG)
                        // ==========================================
                        // Bài Viết (Posts)
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/posts/**").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/**").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/posts/*/submit").hasAnyRole("AUTHOR", "ADMIN")

                        // Đánh giá (Reviews)
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/reviews/**").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/reviews/*/submit").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers("/api/v1/reviews/mine").hasAnyRole("AUTHOR", "ADMIN")

                        // Bình luận, Tương tác & Upload file
                        .requestMatchers(HttpMethod.POST, "/api/v1/comments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/interactions/**").authenticated()
                        .requestMatchers("/api/v1/files/upload").hasAnyRole("AUTHOR", "ADMIN")

                        // ==========================================
                        // 5. CHỐT CHẶN CUỐI CÙNG
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
