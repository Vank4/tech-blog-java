package com.techblog.domain.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileServiceImpl implements FileService {

    // Thư mục chứa ảnh nằm ngay trong project
    private final String UPLOAD_DIR = "uploads/";

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 1. Tạo thư mục nếu chưa có
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 2. Tạo tên file độc nhất để không bị ghi đè (VD: 123e4567-e89b..._anh.jpg)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // 3. Copy file từ Request vào thư mục máy tính
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

            // 4. Trả về đường dẫn (URL) để lưu vào Database
            return "/uploads/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage());
        }
    }
}