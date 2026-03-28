package com.techblog.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedRequest {
    // Lưu ý: Đặt tên là 'featured' (bỏ chữ 'is' đi) để Lombok tạo hàm isFeatured() chuẩn nhất
    private boolean featured;
    private int priority;
}