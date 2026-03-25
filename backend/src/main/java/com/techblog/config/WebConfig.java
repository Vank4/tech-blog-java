package com.techblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Cấu hình cho phép Frontend đọc ảnh
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ đường dẫn URL "/uploads/..." vào thư mục vật lý "uploads/" trong máy
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}