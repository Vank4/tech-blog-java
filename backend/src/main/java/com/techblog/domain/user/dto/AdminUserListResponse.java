package com.techblog.domain.user.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserListResponse {
    private List<AdminUserSummaryResponse> content;
    private int page;
    private int size;
    private long totalElements;
}
