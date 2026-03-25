package com.techblog.domain.file.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')") // Chỉ có người trong nhà mới được up ảnh
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.uploadImage(file);
        return ResponseEntity.ok(new ApiResponse<>(true, "Upload thành công", fileUrl));
    }
}