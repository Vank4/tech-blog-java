package com.techblog.domain.user.dto;

import com.techblog.common.enums.RoleName;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Data;

@Data
public class AssignRolesRequest {

    @NotEmpty(message = "Roles must not be empty")
    private Set<RoleName> roles;
}
