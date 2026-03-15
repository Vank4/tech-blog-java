package com.techblog.domain.user.controller;

import com.techblog.domain.user.dto.ChangePasswordRequest;
import com.techblog.domain.user.dto.UpdateProfileRequest;
import com.techblog.domain.user.dto.UserProfileResponse;
import com.techblog.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyProfile(Authentication authentication) {
        UserProfileResponse response = userService.getMyProfile(authentication.getName());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Get profile successful",
                "data", response
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updateMyProfile(Authentication authentication,
                                                               @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse response = userService.updateMyProfile(authentication.getName(), request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Update profile successful",
                "data", response
        ));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(Authentication authentication,
                                                              @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Change password successful"
        ));
    }
}