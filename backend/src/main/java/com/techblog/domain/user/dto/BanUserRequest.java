package com.techblog.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BanUserRequest {

    @NotBlank(message = "Ban reason is required")
    @Size(max = 500, message = "Ban reason must be at most 500 characters")
    private String reason;
}
