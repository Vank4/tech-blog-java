package com.techblog.domain.auth.controller;

import com.techblog.domain.auth.dto.AuthResponse;
import com.techblog.domain.auth.dto.ForgotPasswordRequest;
import com.techblog.domain.auth.dto.LoginRequest;
import com.techblog.domain.auth.dto.RegisterRequest;
import com.techblog.domain.auth.dto.ResetPasswordRequest;
import com.techblog.domain.auth.dto.ResendVerificationRequest;
import com.techblog.domain.auth.service.AuthService;
import com.techblog.domain.auth.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản trước khi đăng nhập."
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đăng nhập thành công",
                "data", response
        ));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Xác thực email thành công"
        ));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, Object>> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        emailVerificationService.resendVerification(request.getEmail());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đã gửi lại email xác thực thành công"
        ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đã gửi liên kết đặt lại mật khẩu"
        ));
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<Map<String, Object>> validateResetToken(@RequestParam String token) {
        authService.validateResetToken(token);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Token đặt lại mật khẩu hợp lệ"
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đặt lại mật khẩu thành công"
        ));
    }
}
