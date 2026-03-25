package com.techblog.common.enums;

public enum PostStatus {
    DRAFT,      // Nháp
    PENDING,    // Chờ duyệt
    PUBLISHED,  // Đã xuất bản
    REJECTED,   // Bị từ chối (Admin trả về yêu cầu sửa)
    HIDDEN,     // Tạm ẩn
    DELETED     // Đã xóa mềm
}
