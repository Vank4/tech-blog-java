package com.techblog.domain.user.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserSummaryResponse {
    private Long id;
    private String email;
    private String fullName;
    private String status;
    private Boolean emailVerified;
    private Set<String> roles;
    private LocalDateTime createdAt;
}
