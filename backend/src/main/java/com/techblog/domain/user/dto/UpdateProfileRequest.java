package com.techblog.domain.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;

    @Size(max = 255, message = "Avatar URL must be at most 255 characters")
    private String avatarUrl;

    @Size(max = 500, message = "Bio must be at most 500 characters")
    private String bio;
}