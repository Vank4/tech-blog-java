package com.techblog.common.audit;

// import org.springframework.data.annotation.CreatedBy;
// import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.LastModifiedBy;
// import org.springframework.data.annotation.LastModifiedDate;
// import java.time.LocalDateTime;

/**
 * Auditable Base Class
 * Các entity kế thừa class này sẽ tự động có các trường audit:
 * - createdAt: thời gian tạo
 * - updatedAt: thời gian cập nhật
 * - createdBy: người tạo
 * - updatedBy: người cập nhật
 *
 * Yêu cầu: Enable MongoDB auditing trong MongoConfig
 * 
 * @EnableMongoAuditing
 */
public abstract class Auditable {

    // @CreatedDate
    // private LocalDateTime createdAt;

    // @LastModifiedDate
    // private LocalDateTime updatedAt;

    // @CreatedBy
    // private String createdBy;

    // @LastModifiedBy
    // private String updatedBy;

    // TODO: Uncomment và thêm getters/setters khi enable auditing

}
